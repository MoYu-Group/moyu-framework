package io.github.moyugroup.framework.sdk.config;

import io.github.moyugroup.framework.sdk.constant.enums.EnvironmentEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 摸鱼登录 属性配置
 * <p>
 * Created by fanfan on 2023/09/03.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "moyu.auth.client")
public class SSOClientProperties {

    /**
     * 客户端应用appId，必填
     */
    private String appId;
    /**
     * 客户端应用密钥，必填
     */
    private String appSecret;
    /**
     * 环境配置，必填，不同环境调用的sso登录接口不一样
     * see {@link EnvironmentEnum}
     */
    private String environment;
    /**
     * 自定义 sso 服务器地址
     */
    private String serverUrl;

}
