package io.github.moyugroup.exception;

import io.github.moyugroup.base.model.enums.ExceptionEnum;
import io.github.moyugroup.base.model.enums.ExceptionLevel;
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

    /**
     * 异常等级
     */
    private ExceptionLevel level;

    /**
     * 带参构造，不指定异常等级
     *
     * @param code
     * @param message
     */
    public BizException(String code, String message) {
        super(message);
        this.code = code;
        // 未指定异常等级默认为 WARN 异常
        this.level = ExceptionLevel.WARN;
    }

    /**
     * 带参构造，不指定异常等级
     *
     * @param code
     * @param message
     */
    public BizException(String code, String message, ExceptionLevel level) {
        super(message);
        this.code = code;
        this.level = level;
    }

    public BizException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public BizException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMessage());
        this.code = exceptionEnum.getCode();
        this.level = exceptionEnum.getLevel();
    }

    public BizException(ExceptionEnum exceptionEnum, Throwable cause) {
        super(exceptionEnum.getMessage(), cause);
        this.code = exceptionEnum.getCode();
        this.level = exceptionEnum.getLevel();
    }

}
