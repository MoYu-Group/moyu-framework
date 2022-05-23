package io.github.moyugroup.base.model.enums;

/**
 * 异常基类
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
}
