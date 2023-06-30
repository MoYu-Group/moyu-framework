package io.github.moyugroup.web.filter;

import io.github.moyugroup.web.util.TraceIdMdcUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 日志MDC过滤器
 * 通过过滤器来对每个请求链路添加唯一标识 TraceId
 * <p>
 * Created by fanfan on 2023/04/05.
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@WebFilter(filterName = "LogFilter", urlPatterns = "/*")
public class LogMdcFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("LogMdcFilter init");
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest reqs = (HttpServletRequest) servletRequest;
        final HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String traceId = reqs.getHeader(TraceIdMdcUtil.TRACE_ID);
        if (StringUtils.isBlank(traceId)) {
            traceId = TraceIdMdcUtil.getRequestId();
        }
        // MDC 添加 TraceId
        MDC.put(TraceIdMdcUtil.TRACE_ID, traceId);
        try {
            // 在响应头添加 TraceId
            resp.addHeader(TraceIdMdcUtil.TRACE_ID, traceId);
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            MDC.remove(TraceIdMdcUtil.TRACE_ID);
        }
    }

    @Override
    public void destroy() {
        log.info("LogMdcFilter destroy");
        MDC.clear();
        Filter.super.destroy();
    }

}
