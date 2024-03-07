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

    SUCCESS("00000", "请求成功", ExceptionLevel.INFO),

    /**
     * A 表示错误来源于用户
     */
    CLIENT_ERROR("A0001", "用户端错误", ExceptionLevel.INFO),
    USER_REGISTRATION_ERROR("A0100", "用户端错误", ExceptionLevel.INFO),
    USERNAME_VERIFICATION_FAILED("A0110", "用户名校验失败", ExceptionLevel.INFO),
    USERNAME_ALREADY_EXISTS("A0111", "用户名已存在", ExceptionLevel.INFO),
    USERNAME_CONTAIN_SENSITIVE_WORDS("A0112", "用户名包含敏感词", ExceptionLevel.INFO),
    USERNAME_CONTAINS_SPECIAL_CHARACTERS("A0113", "用户名包含特殊字符", ExceptionLevel.INFO),
    PASSWORD_VERIFICATION_FAILED("A0120", "密码校验失败", ExceptionLevel.INFO),
    PASSWORD_LENGTH_IS_NOT_ENOUGH("A0121", "密码长度不够", ExceptionLevel.INFO),
    PASSWORD_STRENGTH_IS_NOT_ENOUGH("A0122", "密码强度不够", ExceptionLevel.INFO),
    INCORRECT_VERIFICATION_CODE("A0130", "验证码输入错误", ExceptionLevel.INFO),
    CAPTCHA_ATTEMPTS_EXCEEDED_LIMIT("A0132", "验证码尝试次数超限", ExceptionLevel.INFO),
    SMS_VERIFICATION_CODE_INPUT_ERROR("A0131", "短信验证码输入错误", ExceptionLevel.INFO),
    EMAIL_VERIFICATION_CODE_INPUT_ERROR("A0132", "邮件验证码输入错误", ExceptionLevel.INFO),
    VOICE_VERIFICATION_CODE_INPUT_ERROR("A0132", "语音验证码输入错误", ExceptionLevel.INFO),
    ABNORMAL_USER_CREDENTIALS("A0140", "用户证件异常", ExceptionLevel.INFO),
    USER_BASIC_INFORMATION_VERIFICATION_FAILED("A0150", "用户基本信息校验失败", ExceptionLevel.INFO),
    PHONE_FORMAT_VERIFICATION_FAILED("A0151", "手机格式校验失败", ExceptionLevel.INFO),
    ADDRESS_FORMAT_VERIFICATION_FAILED("A0152", "地址格式校验失败", ExceptionLevel.INFO),
    EMAIL_FORMAT_VERIFICATION_FAILED("A0153", "邮箱格式校验失败", ExceptionLevel.INFO),
    USER_LOGIN_EXCEPTION("A0200", "用户登录异常", ExceptionLevel.INFO),
    USER_ACCOUNT_DOES_NOT_EXIST("A0201", "用户账户不存在", ExceptionLevel.INFO),
    USER_ACCOUNT_IS_FROZEN("A0202", "用户账户被冻结", ExceptionLevel.INFO),
    USER_ACCOUNT_EXPIRED("A0203", "用户账户已作废", ExceptionLevel.INFO),
    WRONG_USER_PASSWORD("A0210", "用户密码错误", ExceptionLevel.INFO),
    THE_NUMBER_OF_INCORRECT_PASSWORDS_EXCEEDS_THE_LIMIT("A0211", "用户输入密码错误次数超限", ExceptionLevel.INFO),
    WRONG_USER_ACCOUNT_OR_PASSWORD("A0215", "用户账户/密码错误", ExceptionLevel.INFO),
    USER_IDENTITY_VERIFICATION_FAILED("A0220", "用户身份校验失败", ExceptionLevel.INFO),
    USER_LOGIN_HAS_EXPIRED("A0230", "用户登录已过期", ExceptionLevel.INFO),
    ABNORMAL_ACCESS_RIGHTS("A0300", "访问权限异常", ExceptionLevel.INFO),
    ACCESS_UNAUTHORIZED("A0301", "访问未授权", ExceptionLevel.INFO),
    AUTHORIZATION_HAS_EXPIRED("A0311", "授权已过期", ExceptionLevel.INFO),
    NO_PERMISSION_TO_USE_THE_API("A0312", "无权限使用 API", ExceptionLevel.INFO),
    USER_ACCESS_IS_BLOCKED("A0320", "用户访问被拦截", ExceptionLevel.INFO),
    BLACKLIST_USERS("A0321", "黑名单用户", ExceptionLevel.INFO),
    ACCOUNT_IS_FROZEN("A0322", "账号被冻结", ExceptionLevel.INFO),
    ILLEGAL_IP_ADDRESS("A0323", "非法 IP 地址", ExceptionLevel.INFO),
    GATEWAY_ACCESS_IS_LIMITED("A0324", "网关访问受限", ExceptionLevel.INFO),
    SERVICE_IS_IN_ARREARS("A0330", "服务已欠费", ExceptionLevel.INFO),
    USER_SIGNATURE_EXCEPTION("A0340", "用户签名错误", ExceptionLevel.INFO),
    RSA_SIGNATURE_ERROR("A0341", "RSA 签名错误", ExceptionLevel.INFO),
    USER_REQUEST_PARAMETER_ERROR("A0400", "用户请求参数错误", ExceptionLevel.INFO),
    CONTAINS_ILLEGAL_AND_MALICIOUS_JUMP_LINKS("A0401", "包含非法恶意跳转链接", ExceptionLevel.INFO),
    INVALID_USER_INPUT("A0402", "无效的用户输入", ExceptionLevel.INFO),
    REQUEST_METHOD_NOT_ALLOWED("A0405", "请求方法不允许", ExceptionLevel.INFO),
    REQUEST_REQUIRED_PARAMETER_IS_EMPTY("A0410", "请求必填参数为空", ExceptionLevel.INFO),
    USER_ORDER_NUMBER_IS_EMPTY("A0411", "用户订单号为空", ExceptionLevel.INFO),
    MISSING_TIMESTAMP_PARAMETER("A0413", "缺少时间戳参数", ExceptionLevel.INFO),
    INVALID_TIMESTAMP_PARAMETER("A0414", "非法的时间戳参数", ExceptionLevel.INFO),
    PARAMETER_FORMAT_MISMATCH("A0421", "参数格式不匹配", ExceptionLevel.INFO),
    TIME_OUT_OF_SERVICE("A0423", "时间不在服务范围", ExceptionLevel.INFO),
    AMOUNT_EXCEEDS_LIMIT("A0424", "金额超出限制", ExceptionLevel.INFO),
    QUANTITY_EXCEEDS_LIMIT("A0425", "数量超出限制", ExceptionLevel.INFO),
    REQUEST_JSON_PARSING_FAILED("A0427", "请求 JSON 解析失败", ExceptionLevel.INFO),
    ILLEGAL_USER_INPUT("A0430", "用户输入内容非法", ExceptionLevel.INFO),
    CONTAINS_PROHIBITED_SENSITIVE_WORDS("A0431", "包含违禁敏感词", ExceptionLevel.INFO),
    IMAGE_CONTAINS_PROHIBITED_INFORMATION("A0432", "图片包含违禁信息", ExceptionLevel.INFO),
    USER_PAYMENT_TIMED_OUT("A0441", "用户支付超时", ExceptionLevel.INFO),
    ORDER_IS_CLOSED("A0443", "订单已关闭", ExceptionLevel.INFO),
    USER_REQUEST_SERVICE_EXCEPTION("A0500", "用户请求服务异常", ExceptionLevel.INFO),
    REQUEST_LIMIT_EXCEEDED("A0501", "请求次数超出限制", ExceptionLevel.INFO),
    THE_NUMBER_OF_CONCURRENT_REQUESTS_EXCEEDS_THE_LIMIT("A0502", "请求并发数超出限制", ExceptionLevel.INFO),
    WEBSOCKET_CONNECTION_EXCEPTION("A0504", "WebSocket 连接异常", ExceptionLevel.INFO),
    WEBSOCKET_CONNECTION_LOST("A0505", "WebSocket 连接断开", ExceptionLevel.INFO),
    USER_REPEATED_REQUEST("A0506", "用户重复请求", ExceptionLevel.INFO),
    USER_RESOURCE_EXCEPTION("A0600", "用户资源异常", ExceptionLevel.INFO),
    INSUFFICIENT_ACCOUNT_BALANCE("A0601", "账户余额不足", ExceptionLevel.INFO),
    USER_UPLOAD_FILE_EXCEPTION("A0700", "用户上传文件异常", ExceptionLevel.INFO),
    USER_UPLOAD_FILE_TYPE_MISMATCH("A0701", "用户上传文件类型不匹配", ExceptionLevel.INFO),
    USER_UPLOAD_FILE_IS_TOO_LARGE("A0702", "用户上传文件太大", ExceptionLevel.INFO),
    USER_UPLOAD_IMAGE_IS_TOO_LARGE("A0703", "用户上传图片太大", ExceptionLevel.INFO),
    USER_UPLOAD_VIDEO_IS_TOO_LARGE("A0704", "用户上传视频太大", ExceptionLevel.INFO),
    THE_CURRENT_VERSION_OF_THE_USER_IS_ABNORMAL("A0800", "用户当前版本异常", ExceptionLevel.INFO),
    USER_API_REQUEST_VERSION_MISMATCH("A0805", "用户 API 请求版本不匹配", ExceptionLevel.INFO),
    USER_PRIVACY_UNAUTHORIZED("A0900", "用户隐私未授权", ExceptionLevel.INFO),
    ABNORMAL_USER_EQUIPMENT("A1000", "用户设备异常", ExceptionLevel.INFO),

    /**
     * B 表示错误来源于当前系统
     */
    APPLICATION_ERROR("B0001", "系统执行出错", ExceptionLevel.ERROR),
    SYSTEM_EXECUTION_TIMEOUT("B0100", "系统执行超时", ExceptionLevel.ERROR),
    SYSTEM_ORDER_PROCESSING_TIMED_OUT("B0101", "系统订单处理超时", ExceptionLevel.ERROR),
    SYSTEM_RESOURCE_EXCEPTION("B0300", "系统资源异常", ExceptionLevel.ERROR),

    /**
     * C 表示错误来源于第三方服务
     */
    ERROR_CALLING_THIRD_PARTY_SERVICE("C0001", "调用第三方服务出错", ExceptionLevel.ERROR),
    MIDDLEWARE_SERVICE_ERROR("C0100", "中间件服务出错", ExceptionLevel.ERROR),
    RPC_SERVICE_ERROR("C0110", "RPC 服务出错", ExceptionLevel.ERROR),
    INTERFACE_DOES_NOT_EXIST("C0113", "接口不存在", ExceptionLevel.ERROR),
    MESSAGE_SERVICE_ERROR("C0120", "消息服务出错", ExceptionLevel.ERROR),
    CACHE_SERVICE_ERROR("C0130", "缓存服务出错", ExceptionLevel.ERROR),
    CONFIGURATION_SERVICE_ERROR("C0140", "配置服务出错", ExceptionLevel.ERROR),
    NETWORK_RESOURCE_SERVICE_ERROR("C0150", "网络资源服务出错", ExceptionLevel.ERROR),
    THIRD_PARTY_SYSTEM_EXECUTION_TIMEOUT("C0200", "第三方系统执行超时", ExceptionLevel.ERROR),
    CACHE_SERVICE_TIMED_OUT("C0230", "缓存服务超时", ExceptionLevel.ERROR),
    CONFIGURE_SERVICE_TIMEOUT("C0240", "配置服务超时", ExceptionLevel.ERROR),
    DATABASE_SERVICE_TIMED_OUT("C0250", "数据库服务超时", ExceptionLevel.ERROR),
    OAUTH_SERVICE_TIMED_OUT("C0260", "OAuth 登录服务超时", ExceptionLevel.ERROR),
    DATABASE_SERVICE_ERROR("C0300", "数据库服务出错", ExceptionLevel.ERROR),
    TABLE_DOES_NOT_EXIST("C0311", "表不存在", ExceptionLevel.ERROR),
    COLUMN_DOES_NOT_EXIST("C0312", "列不存在", ExceptionLevel.ERROR),
    DATABASE_DEADLOCK("C0331", "数据库死锁", ExceptionLevel.ERROR),
    PRIMARY_KEY_CONFLICT("C0341", "主键冲突", ExceptionLevel.ERROR),
    NOTIFICATION_SERVICE_ERROR("C0500", "通知服务出错", ExceptionLevel.ERROR),
    SMS_REMINDER_SERVICE_FAILED("C0501", "短信提醒服务失败", ExceptionLevel.ERROR),
    VOICE_ALERT_SERVICE_FAILED("C0502", "语音提醒服务失败", ExceptionLevel.ERROR),
    EMAIL_ALERT_SERVICE_FAILED("C0503", "邮件提醒服务失败", ExceptionLevel.ERROR),
    OAUTH_SERVICE_ERROR("C0600", "OAuth 登录服务出错", ExceptionLevel.ERROR),

    ;

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
