package io.github.moyugroup.web.listener;

import io.github.moyugroup.listener.ApplicationRunAdapter;
import io.github.moyugroup.util.PropertyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 框架启动过程监听
 * <p>
 * Created by fanfan on 2022/05/21.
 */
@Slf4j
@Order(-999)
public class FrameworkRunListener extends ApplicationRunAdapter {

    public static final String CONFIG_MODULE = "framework";

    public FrameworkRunListener(SpringApplication application, String[] args) {
    }

    /**
     * 环境初始化后，创建ApplicationContext之前调用
     *
     * @param bootstrapContext 引导上下文
     * @param environment      环境
     */
    @Override
    public void environmentPrepared(ConfigurableBootstrapContext bootstrapContext, ConfigurableEnvironment environment) {
    // 初始化框架默认配置

        PropertyUtil.loadModuleConfigToEnvironment(environment, CONFIG_MODULE);
    }

}
