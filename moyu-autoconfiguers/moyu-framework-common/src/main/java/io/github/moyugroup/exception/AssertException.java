package io.github.moyugroup.exception;


import io.github.moyugroup.base.model.enums.ExceptionEnum;
import io.github.moyugroup.enums.ErrorCodeEnum;

import java.util.Optional;

/**
 * assert异常类
 * <p>
 * Created by fanfan on 2022/05/21.
 */
public class AssertException extends BizException {
    public AssertException(String message) {
        super("AssertException", message);
    }

    public AssertException(ExceptionEnum exceptionEnum) {
        super(Optional.ofNullable(exceptionEnum).orElse(ErrorCodeEnum.USER_REQUEST_PARAMETER_ERROR));
    }
}
