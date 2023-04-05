package io.github.moyugroup.web.aop;

import io.github.moyugroup.base.model.pojo.Result;
import io.github.moyugroup.web.util.TraceIdMdcUtil;
import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Response 返回值增强
 * 只针对返回值是 Result 包装类进行增强
 * <p>
 * Created by fanfan on 2023/04/05.
 */
@ControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return returnType.getParameterType().isAssignableFrom(Result.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        Result<?> result = (Result<?>) body;
        // Result 增加日志追踪标识 TraceId
        result.setTraceId(MDC.get(TraceIdMdcUtil.TRACE_ID));
        return result;
    }

}
