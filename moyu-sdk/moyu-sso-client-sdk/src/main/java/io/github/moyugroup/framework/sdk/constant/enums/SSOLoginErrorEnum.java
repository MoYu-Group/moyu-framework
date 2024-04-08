package io.github.moyugroup.framework.sdk.constant.enums;

import io.github.moyugroup.base.model.enums.ExceptionEnum;
import io.github.moyugroup.base.model.enums.ExceptionLevel;
import io.github.moyugroup.enums.ErrorCodeEnum;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

/**
 * OAuth 异常枚举
 * <p>
 * Created by fanfan on 2023/09/06.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum SSOLoginErrorEnum implements ExceptionEnum {
    SSO_HOST_CHANGE("A1200", "SSO 登录回调的 host 与当前应用 host 不一致", ExceptionLevel.INFO),
    GET_SSO_USER_ERROR("A1201", "获取 SSO 登录用户信息失败", ExceptionLevel.INFO),
    SSO_USER_APP_ID_ERROR("A1202", "SSO 登录用户 APP_ID 错误", ExceptionLevel.INFO),

    ;

    /**
     * 异常编码
     */
    final String code;
    /**
     * 异常消息
     */
    final String message;

    /**
     * 异常消息
     */
    private final ExceptionLevel level;

    SSOLoginErrorEnum(String code, String message, ExceptionLevel level) {
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
