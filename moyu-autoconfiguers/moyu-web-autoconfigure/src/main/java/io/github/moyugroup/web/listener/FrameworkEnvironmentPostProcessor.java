package io.github.moyugroup.web.listener;

import io.github.moyugroup.util.PropertyUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.io.IOException;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * 实现EnvironmentPostProcessor接口，加载配置文件,同时也为以后动态加载配置文件做准备
 */
@Order(HIGHEST_PRECEDENCE)
public class FrameworkEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private final YamlPropertySourceLoader loader = new YamlPropertySourceLoader();

    /**
     * 动态加载配置文件
     *
     * @param environment 配置文件集合的抽象接口 {@link AbstractEnvironment}
     * @param application SpringApplication实例 {@link SpringApplication}
     */
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment,
        SpringApplication application) {
        Resource path = new ClassPathResource("config/framework.yml");
        PropertySource<?> propertySource = loadYaml(path);
        environment.getPropertySources().addLast(propertySource);
        //刷新工具类中的环境变量配置
        PropertyUtil.updateEnvironment(environment, true);
    }

    /**
     * 加载yaml配置文件
     *
     * @param path 文件路径
     * @return 属性的抽象
     */
    private PropertySource<?> loadYaml(Resource path) {
        Assert.isTrue(path.exists(), () -> "Resource " + path + " does not exist");
        try {
            return this.loader.load("custom-resource", path).get(0);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to load yaml configuration from " + path, ex);
        }
    }

}