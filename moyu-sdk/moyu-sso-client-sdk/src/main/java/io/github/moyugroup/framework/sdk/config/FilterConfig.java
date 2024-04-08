package io.github.moyugroup.framework.sdk.config;

import io.github.moyugroup.framework.sdk.filter.MoYuClientSSOLoginFilter;
import jakarta.annotation.Resource;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 过滤器注册
 * <p>
 * Created by fanfan on 2024/03/29.
 */
@Slf4j
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilterConfig {

    @Resource
    SSOClientProperties ssoClientProperties;

    /**
     * 过滤器注册
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<MoYuClientSSOLoginFilter> ssoFilterRegistration() {
        FilterRegistrationBean<MoYuClientSSOLoginFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new MoYuClientSSOLoginFilter(ssoClientProperties));
        // 配置过滤器的路径
        registration.addUrlPatterns("/*");
        // 设置过滤器的顺序
        registration.setOrder(1);
        return registration;
    }

}
