package io.github.moyugroup.base.model.pojo;

import io.github.moyugroup.base.model.enums.ExceptionEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 全局统一返回结果封装
 * <p>
 * Created by fanfan on 2022/05/23.
 */
@Getter
@Setter
public class Result<T> implements Serializable {

    /**
     * 业务是否正常处理完成
     */
    private boolean success;

    /**
     * 业务处理状态代码
     */
    private String code = "0";

    /**
     * 业务处理状态附加信息，如错误信息
     */
    private String message;

    /**
     * 返回的业务数据
     */
    private T content;

    /**
     * 基本构造
     * code 默认为 0
     *
     * @param success 是否成功
     * @param message 返回消息
     * @param content 返回数据
     */
    public Result(boolean success, String message, T content) {
        this.success = success;
        this.message = message;
        this.content = content;
    }

    /**
     * 全参构造
     *
     * @param success 是否成功
     * @param code    返回代码
     * @param message 返回消息
     * @param content 返回数据
     */
    public Result(boolean success, String code, String message, T content) {
        this(success, message, content);
        this.code = code;
    }

    /**
     * 构造
     *
     * @return Result<T>
     */
    public static <T> Result<T> success() {
        return new Result<>(true, "", null);
    }

    /**
     * 构造
     *
     * @param data 返回数据
     * @return Result<T>
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(true, "", data);
    }

    /**
     * 构造
     *
     * @param message 返回消息
     * @param data    返回数据
     * @return Result<T>
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(true, message, data);
    }

    /**
     * 构造
     *
     * @param code    返回代码
     * @param message 返回消息
     * @param data    返回数据
     * @return Result<T>
     */
    public static <T> Result<T> success(String code, String message, T data) {
        return new Result<>(true, code, message, data);
    }

    /**
     * 构造
     *
     * @return Result<T>
     */
    public static <T> Result<T> fail() {
        return new Result<>(false, "", null);
    }

    /**
     * 构造
     *
     * @param exceptionEnum 自定义枚举返回值
     * @return Result<T>
     */
    public static <T> Result<T> fail(ExceptionEnum exceptionEnum) {
        return new Result<>(false, exceptionEnum.getCode(), exceptionEnum.getMessage(), null);
    }

    /**
     * 构造
     *
     * @param data 返回数据
     * @return Result<T>
     */
    public static <T> Result<T> fail(T data) {
        return new Result<>(false, "", data);
    }

    /**
     * 构造
     *
     * @param message 返回消息
     * @param data    返回数据
     * @return Result<T>
     */
    public static <T> Result<T> fail(String message, T data) {
        return new Result<>(false, message, data);
    }

    /**
     * 构造
     *
     * @param code    返回代码
     * @param message 返回消息
     * @param data    返回数据
     * @return Result<T>
     */
    public static <T> Result<T> fail(String code, String message, T data) {
        return new Result<>(false, code, message, data);
    }
}

