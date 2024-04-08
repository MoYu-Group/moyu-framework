package io.github.moyugroup.framework.sdk.constant.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * 配置环境定义
 * <p>
 * Created by fanfan on 2024/04/06.
 */
@Getter
public enum EnvironmentEnum {

    /**
     * 开发环境
     */
    DEVELOP("dev"),
    /**
     * 生产环境
     */
    PRODUCTION("prod"),
    ;

    private String code;

    EnvironmentEnum(String code) {
        this.code = code;
    }

    /**
     * 根据值查找
     *
     * @param code
     * @return
     */
    public static EnvironmentEnum getByCode(String code) {
        return Arrays.stream(EnvironmentEnum.values()).filter(x -> x.getCode().equals(code)).findFirst().orElse(null);
    }
}
