package io.github.moyugroup.enums;

import io.github.moyugroup.base.model.enums.ExceptionEnum;
import io.github.moyugroup.base.model.enums.ExceptionLevel;

import java.util.Objects;

/**
 * 通用错误码定义
 * <p>
 * Created by fanfan on 2022/05/21.
 */
public enum ErrorCodeEnum implements ExceptionEnum {

    /**
     * 全局错误
     */
    APPLICATION_ERROR("-99", "系统繁忙", ExceptionLevel.ERROR),
    /**
     * 请求成功
     */
    SUCCESS("200", "success", ExceptionLevel.INFO),
    /**
     * 请求错误
     */
    REQUEST_ERROR("400", "request error", ExceptionLevel.WARN),
    /**
     * 请求认证错误
     */
    REQUEST_AUTH_ERROR("401", "request auth error", ExceptionLevel.WARN),
    /**
     * 拒绝请求
     */
    REQUEST_FORBIDDEN("403", "request forbidden", ExceptionLevel.WARN),
    /**
     * 服务未找到
     */
    REQUEST_SERVICE_NOT_FOUND("404", "service not found", ExceptionLevel.WARN),
    /**
     * 不允许使用该方法
     */
    REQUEST_METHOD_NOT_ALLOWED("405", "method not allowed", ExceptionLevel.WARN),
    /**
     * 请求参数错误
     */
    REQUEST_PARAM_ERROR("460", "request parameter error", ExceptionLevel.WARN),
    /**
     * 服务端错误
     */
    SERVER_ERROR("500", "server error", ExceptionLevel.WARN),
    /**
     * 服务不可用
     */
    SERVER_NOT_AVAILABLE("503", "server not available", ExceptionLevel.WARN);

    /**
     * 异常编码
     */
    private final String code;
    /**
     * 异常消息
     */
    private final String message;

    /**
     * 异常消息
     */
    private final ExceptionLevel level;

    ErrorCodeEnum(String code, String message, ExceptionLevel level) {
        this.code = code;
        this.message = message;
        // 未指定的异常等级默认为 WRAN 级别
        this.level = Objects.isNull(level) ? ExceptionLevel.WARN : level;
    }

    /**
     * 根据 code 获取 message
     *
     * @param code ErrorCodeEnum.code
     * @return message
     */
    public static String getMessageByCode(String code) {
        ErrorCodeEnum[] values = ErrorCodeEnum.values();
        for (ErrorCodeEnum value : values) {
            if (Objects.equals(value.getCode(), code)) {
                return value.getMessage();
            }
        }
        return null;
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

    /**
     * 返回异常等级
     *
     * @return level
     */
    @Override
    public ExceptionLevel getLevel() {
        return level;
    }

}
