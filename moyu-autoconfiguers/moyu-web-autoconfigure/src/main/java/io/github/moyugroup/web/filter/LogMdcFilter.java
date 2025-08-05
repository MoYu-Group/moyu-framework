package io.github.moyugroup.web.filter;

import io.github.moyugroup.constant.CommonConstants;
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
@Order(Ordered.HIGHEST_PRECEDENCE + 50)
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
        String traceId = reqs.getHeader(CommonConstants.TRACE_ID_HEADER);
        if (StringUtils.isNotEmpty(traceId)) {
            MDC.put(CommonConstants.TRACE_ID, traceId);
        } else {
            traceId = MDC.get(CommonConstants.TRACE_ID);
        }
        try {
            // servletRequest 添加 traceId
            servletRequest.setAttribute(CommonConstants.TRACE_ID, traceId);
            // 在响应头添加 TraceId
            resp.addHeader(CommonConstants.TRACE_ID_HEADER, traceId);
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            MDC.remove(CommonConstants.TRACE_ID);
        }
    }

    @Override
    public void destroy() {
        log.info("LogMdcFilter destroy");
        MDC.clear();
        Filter.super.destroy();
    }

}
