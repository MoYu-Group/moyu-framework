package io.github.moyugroup.exception;

/**
 * 异常编码接口
 * <p>
 * Created by fanfan on 2022/05/21.
 */
public interface ExceptionEnum {

    /**
     * 返回异常编码
     *
     * @return
     */
    String getCode();

    /**
     * 返回异常消息
     *
     * @return
     */
    String getMessage();

}
