package io.github.moyugroup.web.aop;

import cn.hutool.json.JSONUtil;
import io.github.moyugroup.constant.CommonConstants;
import io.github.moyugroup.util.MonitorLogUtil;
import io.github.moyugroup.util.PropertyUtil;
import io.github.moyugroup.web.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 方法运行监控日志 拦截器实现
 * <p>
 * Created by fanfan on 2022/05/21.
 */
@Slf4j
public class MethodTimeAopInterceptor implements MethodInterceptor {

    /**
     * 框架定义aop切面排除的地址
     */
    private String frameworkExclusions;
    /**
     * 用户自定义的配置
     */
    private MethodTimeProperties methodTimeProperties;

    public MethodTimeAopInterceptor(String frameworkExclusions, MethodTimeProperties methodTimeProperties) {
        this.frameworkExclusions = frameworkExclusions;
        this.methodTimeProperties = methodTimeProperties;
    }

    /**
     * 切面拦截处理
     *
     * @param invocation 方法调用
     * @return 方法执行后的返回结果
     * @throws Throwable
     */
    @Nullable
    @Override
    public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
        StopWatch clock = new StopWatch();
        clock.start();
        Object result = null;
        Boolean flag = Boolean.TRUE;
        Exception exception = null;
        try {
            result = invocation.proceed();
        } catch (Exception ex) {
            flag = Boolean.FALSE;
            exception = ex;
            throw ex;
        } finally {
            methodTimeLog(invocation, clock, result, flag, exception);
        }
        return result;
    }

    /**
     * 记录方法运行日志
     *
     * @param invocation 方法调用
     * @param clock      方法执行计时器
     */
    private void methodTimeLog(MethodInvocation invocation, StopWatch clock, Object result, Boolean flag, Exception exception) {
        clock.stop();
        HttpServletRequest request = WebUtil.getRequest();

        String requestUri = Objects.isNull(request) ? "" : request.getRequestURI();
        if (!WebUtil.isMatched(requestUri, this.frameworkExclusions, this.methodTimeProperties.getExclusions())) {
            String httpMethod = Objects.isNull(request) ? "" : request.getMethod();
            Method method = invocation.getMethod();
            String className = method.getDeclaringClass().getName();
            String methodName = method.getName();
            String param = "";
            String resultStr = JSONUtil.toJsonStr(result);
            resultStr = cutParam(resultStr);
            String ip = "";
            if (Objects.nonNull(request)) {
                param = getRequestParams(request, invocation);
                ip = WebUtil.getIpAddress(request);
            }
            String errMsg = "";
            if (Objects.nonNull(exception)) {
                errMsg = exception.getMessage();
            }

            // 在控制台记录方法执行日志
            // IP地址|httpMethod|请求地址|是否成功|请求耗时|类名|方法名|参数|返回值|错误日志
            if (flag) {
                log.info("{}|{}|RequestUri:{}|{}|{}ms|{}|{}|Param:{}|Result:{}|{}",
                        ip, httpMethod, requestUri, true, clock.getTime(), className, methodName, param, resultStr, errMsg);
            } else {
                log.error("{}|{}|RequestUri:{}|{}|{}ms|{}|{}|Param:{}|Result:{}|{}",
                        ip, httpMethod, requestUri, false, clock.getTime(), className, methodName, param, resultStr, errMsg);
            }


            // 监控日志埋点
            // 如果引入了 MoYu 框架的日志配置，会单独将监控的日志输出到 ${logPath}/logs/monitor/operation.log 文件中，便于单独进行日志分析
            // 否则会将日志输出到 console，除非自己指定 loggerName=MONITOR_LOG 的 logback 配置
            String appName = PropertyUtil.getProperty(CommonConstants.PROJECT_NAME, String.class, null);
            MonitorLogUtil.printLog(appName, className, methodName, flag,
                    clock.getTime(), ip, requestUri, param, resultStr, errMsg);
        }
    }

    /**
     * 解析需要记录的请求参数
     *
     * @param request    请求对象
     * @param invocation 方法调用
     * @return 解析到的请求参数
     */
    private String getRequestParams(HttpServletRequest request, MethodInvocation invocation) {
        StringBuilder params = new StringBuilder();
        if (Objects.nonNull(request) && !request.getParameterMap().isEmpty()) {
            //打印请求参数
            Map<String, String> paramMap = new HashMap<>(16);
            for (String key : request.getParameterMap().keySet()) {
                paramMap.put(key, request.getParameter(key));
            }
            params.append(JSONUtil.toJsonStr(paramMap));
        } else {
            //请求参数不存在时尝试打印请求体参数
            for (Object arg : invocation.getArguments()) {
                String argJson = toJsonString(arg);
                if (StringUtils.isNotBlank(argJson)) {
                    params.append(argJson).append(" ");
                }
            }
        }

        //限制参数字符长度，避免某些场景输出失控
        cutParam(params);
        return params.toString();
    }

    /**
     * 参数截断 StringBuilder
     *
     * @param param 需要截断的参数
     */
    private void cutParam(StringBuilder param) {
        if (param.length() > this.methodTimeProperties.getParamsMaxLength()) {
            param.setLength(this.methodTimeProperties.getParamsMaxLength());
        }
    }

    /**
     * 字符串参数截断
     *
     * @param param 需要截断的参数
     * @return 截断后的字符串
     */
    private String cutParam(String param) {
        if (StringUtils.isBlank(param)) {
            return "";
        }
        StringBuilder sb = new StringBuilder(param);
        cutParam(sb);
        return sb.toString();
    }

    /**
     * 将请求参数转为字符串
     *
     * @param arg 请求参数
     * @return 转换后的字符串
     */
    private String toJsonString(Object arg) {
        if (arg instanceof HttpServletRequest || arg instanceof HttpServletResponse || arg instanceof ModelMap
                || arg instanceof Exception || arg instanceof MultipartFile) {
            //避免类似富文本的超长参数输出
            return "";
        } else {
            return JSONUtil.toJsonStr(arg);
        }
    }
}
