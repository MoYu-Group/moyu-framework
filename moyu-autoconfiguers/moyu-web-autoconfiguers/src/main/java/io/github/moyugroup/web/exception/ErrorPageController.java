package io.github.moyugroup.web.exception;

import cn.hutool.core.bean.BeanUtil;
import io.github.moyugroup.base.model.pojo.Result;
import io.github.moyugroup.web.util.TraceIdMdcUtil;
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
     * 页面异常模型渲染
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = super.errorHtml(request, response);
        return fillExceptionMessage(request, modelAndView);
    }

    /**
     * 填充请求异常信息到视图中
     *
     * @param request
     * @param modelAndView
     * @return
     */
    private ModelAndView fillExceptionMessage(HttpServletRequest request, ModelAndView modelAndView) {
        String requestExceptionMessage = getRequestException(request);
        Map<String, Object> model = modelAndView.getModel();
        model.put("message", requestExceptionMessage);
        return new ModelAndView(modelAndView.getViewName(), model, modelAndView.getStatus());
    }

    /**
     * 获取请求异常信息
     *
     * @param request
     * @return
     */
    private String getRequestException(HttpServletRequest request) {
        Object exception = request.getAttribute("jakarta.servlet.error.exception");
        if (Objects.nonNull(exception) && exception instanceof Exception ex) {
            return ex.getMessage();
        }
        return null;
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
        HttpStatus status = super.getStatus(request);
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

}
