package io.github.moyugroup.web.exception;


import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import io.github.moyugroup.enums.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * 未进入Controller的异常在此处理，返回默认错误页面
 * <p>
 * Created by fanfan on 2022/05/29.
 */
@Slf4j
@Controller
public class ErrorPageExceptionHandler implements ErrorController {

    private static final String ERROR_PATH = "error";

    @Resource
    private ErrorAttributes errorAttributes;

    /**
     * 跳转到默认错误页面
     *
     * @param request      request
     * @param modelAndView modelAndView
     * @return 错误页面
     */
    @RequestMapping(ERROR_PATH)
    public Object error(HttpServletRequest request, ModelAndView modelAndView) {
        ServletWebRequest servletWebRequest = new ServletWebRequest(request);
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(servletWebRequest, ErrorAttributeOptions.defaults());
        Object status = errorAttributes.get("status");
        modelAndView.addObject("status", status);
        String messageByCode = ErrorCodeEnum.getMessageByCode(String.valueOf(status));
        modelAndView.addObject("message", Objects.isNull(messageByCode) ? errorAttributes.get("message") : messageByCode);
        Date timestamp = (Date) errorAttributes.get("timestamp");
        modelAndView.addObject("timestamp", DateUtil.format(timestamp, DatePattern.NORM_DATETIME_FORMAT));
        modelAndView.addObject("path", errorAttributes.get("path"));
        modelAndView.addObject("error", errorAttributes.get("error"));
        modelAndView.setViewName("error");
        return modelAndView;
    }
}
