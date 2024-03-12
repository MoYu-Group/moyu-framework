package io.github.moyugroup.web.exception;

import io.github.moyugroup.base.model.pojo.Result;
import io.github.moyugroup.enums.ErrorCodeEnum;
import io.github.moyugroup.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 全局统一异常处理，进入Controller层的异常，会在此进行统一处理
 * 在应用中可以继承此类进行进行异常处理扩展，只需要在自定义类上加上 @RestControllerAdvice 注解即可
 * <p>
 * Created by fanfan on 2022/05/26.
 */
@Slf4j
@RestControllerAdvice
@ResponseStatus(HttpStatus.OK)
public class GlobalExceptionHandler {

    /**
     * 业务异常，抛出异常信息
     *
     * @param ex BizException
     * @return 异常信息
     */
    @ExceptionHandler({BizException.class})
    public Result<?> handlerBusinessException(BizException ex) {
        log.warn("encounter error|BusinessException|code={}|message={}", ex.getCode(), ex.getMessage());
        return Result.error(ex.getCode(), ex.getMessage());
    }

    /**
     * 方法不允许 异常
     *
     * @param ex HttpRequestMethodNotSupportedException
     * @return 异常信息
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public Result<?> handlerHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.warn("encounter error|HttpRequestMethodNotSupportedException|message={}", ex.getMessage());
        return Result.error(ErrorCodeEnum.REQUEST_METHOD_NOT_ALLOWED.getCode(), ex.getMessage());
    }

    /**
     * 参数异常
     * Validated 参数校验异常
     *
     * @param ex MethodArgsException
     * @return 异常信息
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Result<?> handlerMethodArgsException(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getFieldErrors();
        String warnMessage = fieldErrors.stream().flatMap(fieldError -> Stream.of(fieldError.getField() + ":" + fieldError.getDefaultMessage())).collect(Collectors.joining(";"));
        log.warn("encounter error|MethodArgumentNotValidException|message={}", warnMessage);
        String message = ErrorCodeEnum.USER_REQUEST_PARAMETER_ERROR.getMessage() + ":" + fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(","));
        return Result.error(ErrorCodeEnum.USER_REQUEST_PARAMETER_ERROR.getCode(), message);
    }

    /**
     * HTTP请求参数转换异常
     *
     * @param ex HttpMessageNotReadableException
     * @return 异常信息
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public Result<?> handlerMethodArgsException(HttpMessageNotReadableException ex) {
        log.warn("encounter error|HttpMessageNotReadableException|message={}", ex.getMessage());
        return Result.error(ErrorCodeEnum.USER_REQUEST_PARAMETER_ERROR.getCode(), "HTTP请求参数转换异常");
    }

    /**
     * 请求资源不存在异常
     *
     * @param ex NoResourceFoundException
     * @return 异常信息
     */
    @ExceptionHandler({NoResourceFoundException.class})
    public Result<?> handlerMethodArgsException(NoResourceFoundException ex) {
        log.warn("encounter error|NoResourceFoundException|message={}", ex.getMessage());
        return Result.error(ErrorCodeEnum.APPLICATION_ERROR.getCode(), ex.getMessage());
    }

    /**
     * 其他异常，抛出异常信息
     * 生产环境建议对外只抛出系统繁忙
     *
     * @param ex Exception
     * @return 系统繁忙
     */
    @ExceptionHandler({Exception.class})
    public Result<?> handlerException(Exception ex) {
        log.error("encounter error", ex);
        return Result.error(ErrorCodeEnum.APPLICATION_ERROR.getCode(), ex.getMessage());
    }

}
