package io.github.moyugroup.web.exception;

import cn.hutool.core.bean.BeanUtil;
import io.github.moyugroup.base.model.pojo.Result;
import io.github.moyugroup.web.util.TraceIdMdcUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Error 处理器，未进入 Controller 的异常在此处理，重写 Spring 默认实现
 * <p>
 * Created by fanfan on 2023/08/03.
 */
@Controller
public class ErrorPageController extends BasicErrorController {

    @Autowired
    public ErrorPageController(ErrorAttributes errorAttributes,
                               ServerProperties serverProperties,
                               List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, serverProperties.getError(), errorViewResolvers);
    }

    /**
     * 页面异常不作处理
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        return super.errorHtml(request, response);
    }

    /**
     * 重写非页面异常返回的 json
     * 保持和全局返回值格式一致
     *
     * @param request
     * @return
     */
    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        Object traceId = request.getAttribute(TraceIdMdcUtil.TRACE_ID);
        Result<?> result = new Result<>(false, String.valueOf(status.value()), status.getReasonPhrase(), null);
        setTraceId(result, request);
        Map<String, Object> body = BeanUtil.beanToMap(result);
        return new ResponseEntity<>(body, status);
    }

    private void setTraceId(Result<?> result, HttpServletRequest request) {
        Object traceIdObj = request.getAttribute(TraceIdMdcUtil.TRACE_ID);
        if (Objects.nonNull(traceIdObj) && traceIdObj instanceof String traceId) {
            result.setTraceId(traceId);
        }
    }

    protected HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
