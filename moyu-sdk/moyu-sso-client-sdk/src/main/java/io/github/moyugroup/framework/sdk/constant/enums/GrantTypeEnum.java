package io.github.moyugroup.framework.sdk.constant.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * OAuth2 授权方式
 * <p>
 * Created by fanfan on 2023/09/06.
 */
@Getter
public enum GrantTypeEnum {

    /**
     * 授权码模式
     * 这是最常见的授权类型，通常用于第三方应用程序访问服务器资源。用户首先登录并同意授权给第三方应用，然后应用使用返回的授权码去交换访问令牌。
     */
    AUTHORIZATION_CODE("authorization_code"),
    /**
     * 隐式授权模式
     * 这种授权类型较少使用，因为它不如授权码模式安全。它直接返回访问令牌，省略了交换授权码的步骤，通常用于客户端应用程序。
     */
    IMPLICIT("implicit"),
    /**
     * 密码模式
     * 直接使用用户名和密码获取访问令牌。这种方式简单直接，但不推荐使用，因为它要求应用程序存储用户的密码。
     */
    PASSWORD("password"),
    /**
     * 客户端凭证模式
     * 使用应用程序的自身凭证而不是通过用户身份来获取访问令牌。这适用于无需用户交互的服务器到服务器的通信。
     */
    CLIENT_CREDENTIALS("client_credentials"),
    /**
     * 当访问令牌过期时，可以使用刷新令牌来获取新的访问令牌，而不需要用户再次进行认证。
     */
    REFRESH_TOKEN("refresh_token"),
    ;

    private String code;

    GrantTypeEnum(String code) {
        this.code = code;
    }

    /**
     * 根据值查找
     *
     * @param code
     * @return
     */
    public static GrantTypeEnum getByCode(String code) {
        return Arrays.stream(GrantTypeEnum.values()).filter(x -> x.getCode().equals(code)).findFirst().orElse(null);
    }
}
