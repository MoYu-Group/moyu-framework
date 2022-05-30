package io.github.moyugroup.exception;

import io.github.moyugroup.base.model.pojo.Result;
import io.github.moyugroup.enums.ErrorCodeEnum;
import io.github.moyugroup.web.exception.GlobalExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 继承框架中的全局异常处理以进行业务异常扩展
 * <p>
 * Created by fanfan on 2022/05/26.
 */
@Slf4j
@RestControllerAdvice
@ResponseStatus(HttpStatus.OK)
public class CustomExceptionHandler extends GlobalExceptionHandler {

    /**
     * 重写方法不允许 异常
     *
     * @param ex HttpRequestMethodNotSupportedException
     * @return 异常信息
     */
    @Override
    public Result<?> handlerHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return Result.error(ErrorCodeEnum.REQUEST_METHOD_NOT_ALLOWED);
    }
}
