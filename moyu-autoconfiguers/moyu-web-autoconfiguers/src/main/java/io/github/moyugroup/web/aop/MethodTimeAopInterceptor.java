package io.github.moyugroup.web.aop;

import cn.hutool.json.JSONUtil;
import io.github.moyugroup.base.model.enums.ExceptionLevel;
import io.github.moyugroup.enums.ErrorCodeEnum;
import io.github.moyugroup.exception.BizException;
import io.github.moyugroup.util.LogUtil;
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
        HttpServletRequest request = WebUtil.getRequest();
        String requestUri = Objects.isNull(request) ? "" : request.getRequestURI();
        if (!WebUtil.isMatched(requestUri, this.frameworkExclusions, this.methodTimeProperties.getExclusions())) {
            Method method = invocation.getMethod();
            String httpMethod = Objects.isNull(request) ? "" : request.getMethod();
            String className = method.getDeclaringClass().getName();
            String methodName = method.getName();
            String param = "";
            String resultStr = "";
            String ip = "";
            if (Objects.nonNull(request)) {
                param = getRequestParams(request, invocation);
                ip = WebUtil.getIpAddress(request);
            }
            if (Objects.nonNull(request)) {
                resultStr = JSONUtil.toJsonStr(result);
                resultStr = cutParam(resultStr);
            }
            String errCode = ErrorCodeEnum.SUCCESS.getCode();
            String errMsg = "";
            ExceptionLevel logLevel = ExceptionLevel.INFO;
            if (Objects.nonNull(exception)) {
                if (exception instanceof BizException bizException) {
                    errCode = bizException.getCode();
                    logLevel = bizException.getLevel();
                } else {
                    errCode = ErrorCodeEnum.APPLICATION_ERROR.getCode();
                    logLevel = ExceptionLevel.ERROR;
                }
                errMsg = exception.getMessage();
            }
            clock.stop();

            // 控制台日志埋点
            LogUtil.printConsoleLog(log, flag, ip, httpMethod, errCode, requestUri, clock.getTime(), className, methodName, param, resultStr, errMsg, logLevel);

            // 监控日志埋点
            LogUtil.printMonitorLog(className, methodName, ip, httpMethod, errCode, requestUri, flag, clock.getTime(), param, resultStr, errMsg, logLevel);
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
