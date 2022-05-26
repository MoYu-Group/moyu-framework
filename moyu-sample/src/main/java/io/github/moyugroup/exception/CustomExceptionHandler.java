package io.github.moyugroup.exception;

import io.github.moyugroup.base.model.pojo.Result;
import io.github.moyugroup.enums.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 重写框架中的全局异常处理逻辑
 * <p>
 * Created by fanfan on 2022/05/26.
 */
@Slf4j
@RestControllerAdvice
@ResponseStatus(HttpStatus.OK)
public class CustomExceptionHandler {

    /**
     * 业务异常，抛出异常信息
     *
     * @param ex BizException
     * @return 异常信息
     */
    @ExceptionHandler({BizException.class})
    public Result<?> handlerBusinessException(BizException ex) {
        log.error("encounter error custom handler|BusinessException|code={}|message={}", ex.getCode(), ex.getMessage());
        return Result.error(ex.getCode(), ex.getMessage());
    }

    /**
     * 其他异常，抛出系统繁忙
     *
     * @param ex Exception
     * @return 系统繁忙
     */
    @ExceptionHandler({Exception.class})
    public Result<?> handlerException(Exception ex) {
        log.error("encounter error custom handler", ex);
        return Result.error(ErrorCodeEnum.APPLICATION_ERROR.getCode(), ErrorCodeEnum.APPLICATION_ERROR.getMessage());
    }

}
