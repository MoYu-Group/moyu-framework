package io.github.moyugroup.exception;

import io.github.moyugroup.base.model.enums.ExceptionEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 业务异常类基类
 * <p>
 * Created by fanfan on 2022/05/21.
 */
@Getter
@Setter
public class BizException extends RuntimeException {

    /**
     * 异常编码
     */
    private String code;

    public BizException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public BizException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMessage());
        this.code = exceptionEnum.getCode();
    }

    public BizException(ExceptionEnum exceptionEnum, Throwable cause) {
        super(exceptionEnum.getMessage(), cause);
        this.code = exceptionEnum.getCode();
    }

}
