package io.github.moyugroup.web.aop;

import cn.hutool.json.JSONUtil;
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
        Object result = null;
        StopWatch clock = new StopWatch();
        clock.start();
        try {
            result = invocation.proceed();
        } finally {
            methodTimeLog(invocation, clock, result);
        }
        return result;
    }

    /**
     * 记录方法运行日志
     *
     * @param invocation 方法调用
     * @param clock      方法执行计时器
     */
    private void methodTimeLog(MethodInvocation invocation, StopWatch clock, Object result) {
        clock.stop();
        HttpServletRequest request = WebUtil.getRequest();

        String requestUri = (request == null) ? "" : request.getRequestURI();
        if (!WebUtil.isMatched(requestUri, this.frameworkExclusions, this.methodTimeProperties.getExclusions())) {
            String httpMethod = (request == null) ? "" : request.getMethod();
            Method method = invocation.getMethod();
            String signature = method.getDeclaringClass() + "." + method.getName();
            String params = null;
            String ip = null;
            if (Objects.nonNull(request)) {
                params = getRequestParams(request, invocation);
                ip = WebUtil.getIpAddress(request);
            }

            //在控制台记录方法执行日志
            log.info("Http:{}|RequestUri:{}|Params:{}|IP:{}|Time:{}ms|Method:{}",
                    httpMethod, requestUri, params, ip, clock.getTime(), signature);

            // 监控日志埋点，如果引入了 MoYu 框架的日志配置，会单独导出到 logPath/logs/monitor/operation.log 文件中，便于单独进行日志分析
            // 否则会输出到 console，除非自己指定 loggerName=MONITOR_LOG 的配置
            String appName = PropertyUtil.getProperty("spring.application.name", String.class, null);
            String serviceName = method.getDeclaringClass().toString();
            String logName = serviceName.substring(serviceName.lastIndexOf(".") + 1) + "." + method.getName() + ":" + (Objects.isNull(request) ? "" : request.getMethod());
            MonitorLogUtil.printLog(appName, logName, Boolean.TRUE,
                    clock.getTime(), ip, getRequestParams(request, invocation), JSONUtil.toJsonStr(result), null);
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
        if (params.length() > this.methodTimeProperties.getParamsMaxLength()) {
            params.setLength(this.methodTimeProperties.getParamsMaxLength());
        }
        return params.toString();
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
