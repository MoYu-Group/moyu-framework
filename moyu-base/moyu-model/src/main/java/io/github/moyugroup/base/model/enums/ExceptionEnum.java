package io.github.moyugroup.base.model.enums;

/**
 * 异常枚举基类
 * <p>
 * Created by fanfan on 2022/05/23.
 */
public interface ExceptionEnum {

    /**
     * 返回异常Code
     *
     * @return code
     */
    String getCode();

    /**
     * 返回异常信息
     *
     * @return message
     */
    String getMessage();

    /**
     * 返回异常等级
     *
     * @return level
     */
    ExceptionLevel getLevel();
}
