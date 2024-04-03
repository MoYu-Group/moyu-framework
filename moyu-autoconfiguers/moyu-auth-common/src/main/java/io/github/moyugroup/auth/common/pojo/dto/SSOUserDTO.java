package io.github.moyugroup.auth.common.pojo.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * SSO 登录用户信息
 * <p>
 * Created by fanfan on 2024/04/04.
 */
@Data
@Accessors(chain = true)
public class SSOUserDTO {

    /**
     * SSO 登录 AppId
     */
    String appId;

    /**
     * 登录用户信息
     */
    UserInfo user;

}
