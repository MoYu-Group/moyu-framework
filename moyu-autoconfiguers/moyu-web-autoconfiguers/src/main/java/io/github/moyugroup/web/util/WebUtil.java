package io.github.moyugroup.web.util;

import cn.hutool.json.JSONUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.github.moyugroup.base.model.pojo.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.springframework.util.StringUtils.commaDelimitedListToSet;

/**
 * Web工具类
 * <p>
 * Created by fanfan on 2022/05/21.
 */
@Slf4j
public class WebUtil {


    public static final String X_REQUESTED_WIDTH = "X-Requested-With";
    public static final String X_REAL_IP = "X-Real-IP";
    public static final String X_FORWARDED_FOR = "X-Forwarded-For";
    public static final String XML_HTTP_REQUEST = "XMLHttpRequest";
    public static final String UNKNOWN = "unKnown";
    public static final String LOCAL_IPV4 = "127.0.0.1";
    public static final String LOCAL_IPV6 = "0:0:0:0:0:0:0:1";
    public static final Cache<Object, Object> PATTERN_MATCH_CACHE = CacheBuilder.newBuilder().maximumSize(10000)
            .expireAfterAccess(24, TimeUnit.HOURS).build();
    public final static boolean[] identifierFlags = new boolean[256];

    public static boolean isIdent(char ch) {
        return ch < identifierFlags.length && identifierFlags[ch];
    }

    /**
     * 获取http请求中的request对象
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes == null ? null : attributes.getRequest();
    }

    /**
     * 获取http请求中的response对象
     *
     * @return
     */
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes == null ? null : attributes.getResponse();
    }

    /**
     * 判断是否是Ajax请求
     *
     * @return
     */
    public static boolean isAjaxRequest() {
        HttpServletRequest request = getRequest();
        return request != null && XML_HTTP_REQUEST.equals(request.getHeader(X_REQUESTED_WIDTH));
    }

    /**
     * 将对象的json字符串写到response中
     *
     * @param obj
     */
    public static void writeJsonResponse(Object obj) {
        HttpServletResponse response = getResponse();
        HttpServletRequest request = getRequest();
        try {
            if (response != null && request != null) {
                if (response.isCommitted()) {
                    return;
                }
                if (obj instanceof Result<?> result) {
                    Object traceId = request.getAttribute(TraceIdMdcUtil.TRACE_ID);
                    if (Objects.nonNull(traceId)) {
                        result.setTraceId((String) traceId);
                    }
                }
                String results = JSONUtil.toJsonStr(obj);

                response.setCharacterEncoding("UTF-8");
                String callbackFunName = request.getParameter("callback");
                if (StringUtils.isBlank(callbackFunName)) {
                    response.setContentType("application/json;charset=UTF-8");
                    response.setStatus(HttpServletResponse.SC_OK);
                    //没有callback直接输出json格式结果
                    response.getWriter().println(results);
                } else {
                    response.setContentType("text/html;charset=utf-8");
                    //输出jsonp格式的响应
                    if (!isValidJsonpQueryParam(callbackFunName)) {
                        log.error("无效的jsonp回调方法{}，将使用默认回调方法callback", callbackFunName);
                        callbackFunName = "callback";
                    }
                    response.getWriter().println(callbackFunName + "( " + results + " )");
                }

                response.flushBuffer();
            }
        } catch (Exception e) {
            log.error("exception when writeJsonResponse", e);
        }
    }

    public static boolean isValidJsonpQueryParam(String value) {
        if (value == null || value.length() == 0) {
            return false;
        }

        for (int i = 0, len = value.length(); i < len; ++i) {
            char ch = value.charAt(i);
            if (ch != '.' && !isIdent(ch)) {
                return false;
            }
        }

        return true;
    }


    /**
     * 获取当前请求的真实ip
     *
     * @return
     */
    public static String getIpAddress() {
        HttpServletRequest request = getRequest();
        return (request == null) ? "" : getIpAddress(request);
    }

    /**
     * 获取request请求的真实ip
     * 参考 https://imququ.com/post/x-forwarded-for-header-in-http.html 对 X_FORWARDED_FOR 协议头的讲解
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader(X_FORWARDED_FOR);
        if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(X_REAL_IP);
        }

        if (StringUtils.isNotEmpty(ip) && !UNKNOWN.equalsIgnoreCase(ip)) {
            //经过Nginx等反向代理，最后一个为IP原始访问地址，其他IP可被随意伪造，不可信
            String[] split = ip.split(",");
            return split[split.length - 1];
        } else {
            ip = request.getRemoteAddr();
            //如果访问来源是本地环路地址，代表在本地访问，此时直接获取网卡的IP地址
            if (LOCAL_IPV4.equals(ip) || LOCAL_IPV6.equals(ip)) {
                try {
                    InetAddress inet = InetAddress.getLocalHost();
                    ip = inet.getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
            return ip;
        }
    }

    /**
     * 判断传入的text是否满足patterns，对指定的文本进行模糊匹配
     *
     * @param text     要进行模糊匹配的文本
     * @param patterns java正则表达式组，不区分大小写,多个表达式用逗号分隔
     * @return
     */
    public static boolean isMatched(final String text, String... patterns) {
        int cacheKey = Objects.hash(text, StringUtils.join(patterns, ":"));
        try {
            //使用cache，避免每次都解析
            return (boolean) PATTERN_MATCH_CACHE.get(cacheKey, () -> {
                if (text != null && patterns != null) {
                    List<String> patternList = Stream.of(patterns).flatMap(
                            pattern -> commaDelimitedListToSet(pattern).stream()).filter(StringUtils::isNotBlank).toList();
                    return patternList.stream().anyMatch(pattern -> Pattern.compile(pattern, Pattern.DOTALL)
                            .matcher(text.trim()).matches());
                } else {
                    return false;
                }
            });
        } catch (ExecutionException e) {
            log.error("ExecutionException when handling cacheKey", e);
            return false;
        }

    }

    /**
     * 获取程序启动的main方法，jar启动为项目启动类的main方法，war包启动时为tomcat的main方法
     *
     * @return
     */
    public static Class<?> deduceMainApplicationClass() {
        try {
            StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                if ("main".equals(stackTraceElement.getMethodName())) {
                    return Class.forName(stackTraceElement.getClassName());
                }
            }
        } catch (ClassNotFoundException ex) {
            // Swallow and continue
        }
        return null;
    }
}
