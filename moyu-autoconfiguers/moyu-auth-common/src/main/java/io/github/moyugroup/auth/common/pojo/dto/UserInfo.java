package io.github.moyugroup.auth.common.pojo.dto;

import lombok.Data;

/**
 * 用户信息
 * <p>
 * Created by fanfan on 2024/03/13.
 */
@Data
public class UserInfo {
    /**
     * 租户ID
     */
    String tenantId;
    /**
     * 租户名称
     */
    String tenantName;
    /**
     * 用户ID
     */
    String userId;
    /**
     * 用户名
     */
    String username;
    /**
     * 手机号
     */
    String mobile;
    /**
     * 邮箱
     */
    String email;
    /**
     * 用户全名
     */
    String fullName;

}
