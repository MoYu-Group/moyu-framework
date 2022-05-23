package io.github.moyugroup.enums;

import io.github.moyugroup.base.model.enums.ExceptionEnum;

/**
 * 通用错误码定义
 * <p>
 * Created by fanfan on 2022/05/21.
 */
public enum ErrorCodeEnum implements ExceptionEnum {

    /**
     * 请求成功
     */
    SUCCESS("200", "success"),
    /**
     * 请求错误
     */
    REQUEST_ERROR("400", "request error"),
    /**
     * 请求认证错误
     */
    REQUEST_AUTH_ERROR("401", "request auth error"),
    /**
     * 拒绝请求
     */
    REQUEST_FORBIDDEN("403", "request forbidden"),
    /**
     * 服务未找到
     */
    REQUEST_SERVICE_NOT_FOUND("404", "service not found"),
    /**
     * 请求参数错误
     */
    REQUEST_PARAM_ERROR("460", "request parameter error"),
    /**
     * 服务端错误
     */
    SERVER_ERROR("500", "server error"),
    /**
     * 服务不可用
     */
    SERVER_NOT_AVAILABLE("503", "server not available");

    /**
     * 异常编码
     */
    private String code;
    /**
     * 异常消息
     */
    private String message;

    ErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 返回异常编码
     *
     * @return
     */
    @Override
    public String getCode() {
        return code;
    }

    /**
     * 返回异常消息
     *
     * @return
     */
    @Override
    public String getMessage() {
        return message;
    }
}
