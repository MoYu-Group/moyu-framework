package io.github.moyugroup.util;

import io.github.moyugroup.enums.PropertyTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 属性工具类
 * <p>
 * Created by fanfan on 2022/05/21.
 */
@Slf4j
public class PropertyUtil {
    /**
     * 在ApplicationContextInitializer回调后注入，用于框架启动过程中就能获取到框架属性信息
     */
    private static ConfigurableEnvironment environment;

    /**
     * 默认的资源加载器
     */
    private static ResourceLoader defaultResourceLoader = new DefaultResourceLoader();

    /**
     * environment检查，确保environment是初始化了的
     */
    private static void checkEnvironment() {
        Assert.notNull(environment, "该方法只能在environmentPrepared回调后使用");
    }

    /**
     * 获取配置信息，未配置则返回默认值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getProperty(String key, String defaultValue) {
        checkEnvironment();

        return environment.getProperty(key, defaultValue);
    }

    /**
     * 获取配置信息，将其转换为指定类型，未配置则返回默认值
     *
     * @param key
     * @param targetType
     * @param defaultValue
     * @param <T>
     * @return
     */
    public static <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        checkEnvironment();

        return environment.getProperty(key, targetType, defaultValue);
    }

    /**
     * 更新环境变量
     *
     * @param environment
     * @param forceUpdate
     */
    public static void updateEnvironment(ConfigurableEnvironment environment, boolean forceUpdate) {
        if (forceUpdate || PropertyUtil.environment == null) {
            PropertyUtil.environment = environment;
        }
    }

    /**
     * 将指定模块的配置添加到全局的PropertySource的尾部
     *
     * @param configModule 模块名称，配置文件放置在config目录中
     */
    public static void loadModuleConfigToEnvironment(ConfigurableEnvironment environment, String configModule) {
        AssertUtil.hasText(configModule, "configModule不能为空");
        updateEnvironment(environment, true);

        //加载与profile相关的配置
        String[] activeProfiles = environment.getActiveProfiles();
        for (String profile : activeProfiles) {
            tryLoadModuleConfigWithProfile(configModule, profile);
        }

        //加载与profile无关的配置
        tryLoadModuleConfigWithProfile(configModule, null);
    }

    /**
     * 按照profile尝试加载配置文件
     *
     * @param configModule
     * @param profile
     */
    private static void tryLoadModuleConfigWithProfile(String configModule, String profile) {
        Arrays.stream(PropertyTypeEnum.values()).forEach(propertyType -> {
            //解析属性文件路径
            StringBuilder sb = new StringBuilder("classpath:/config/").append(configModule);
            if (StringUtils.isNotBlank(profile)) {
                sb.append("-").append(profile);
            }
            sb.append(".").append(propertyType.getFileExtension());
            String configFilePath = sb.toString();

            //解析得到propertySources
            Resource configFileResource = getClassPathResource(configFilePath);
            if (configFileResource == null) {
                return;
            }

            try {
                List<PropertySource<?>> propertySources = propertyType.getPropertySourceLoader().load(configFilePath,
                        configFileResource);

                //添加到environment的propertySources列表尾部
                addPropertySourceToEnvironment(false, propertySources);
            } catch (Exception e) {
                log.error("Exception when parsing config file", e);
            }
        });
    }

    /**
     * 解析属性配置对象信息
     *
     * @param namePrefix
     * @param propertiesCls
     * @param <T>
     * @return
     */
    public static <T> T parseProperties(String namePrefix, Class<T> propertiesCls) {
        checkEnvironment();
        try {
            BindResult<T> result = Binder.get(environment).bind(namePrefix, propertiesCls);
            return result.get();
        } catch (Exception e) {
            log.info("no properties find in project with prefix:{}", namePrefix);
            try {
                return propertiesCls.newInstance();
            } catch (Exception e1) {
            }
            return null;
        }
    }

    /**
     * 添加propertySource到environment
     *
     * @param propertySources
     * @param addFirst
     */
    public static void addPropertySourceToEnvironment(boolean addFirst, List<PropertySource<?>> propertySources) {
        checkEnvironment();

        if (CollectionUtils.isEmpty(propertySources)) {
            return;
        }

        MutablePropertySources environmentPropertySources = environment.getPropertySources();
        for (PropertySource propertySource : propertySources) {
            if (environmentPropertySources.contains(propertySource.getName())) {
                environmentPropertySources.replace(propertySource.getName(), propertySource);
            } else {
                if (addFirst) {
                    environmentPropertySources.addFirst(propertySource);
                } else {
                    environmentPropertySources.addLast(propertySource);
                }
            }
        }

    }

    /**
     * 占用符替换
     *
     * @param text
     * @return
     */
    public static String parsePlaceHolder(String text) {
        checkEnvironment();
        return environment.resolvePlaceholders(text);
    }

    /**
     * 从classpath中读取资源的Resource
     *
     * @param confFile 资源路径，格式如classpath:xxxx
     * @return null if resource doesn't exits
     */
    public static Resource getClassPathResource(String confFile) {
        Resource resource = defaultResourceLoader.getResource(confFile);
        return resource.exists() ? resource : null;
    }
}
