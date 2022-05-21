package io.github.moyugroup.enums;

import lombok.Getter;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.boot.env.YamlPropertySourceLoader;

/**
 * 配置文件属性类型枚举类
 * <p>
 * Created by fanfan on 2022/05/21.
 */
@Getter
public enum PropertyTypeEnum {
    /**
     * properties
     */
    PROPERTIES("properties", new PropertiesPropertySourceLoader()),
    /**
     * yaml
     */
    YAML("yaml", new YamlPropertySourceLoader()),
    /**
     * yml
     */
    YML("yml", new YamlPropertySourceLoader());
    /**
     * 配置文件扩展名
     */
    private String fileExtension;
    /**
     * 配置文件加载器
     */
    private PropertySourceLoader propertySourceLoader;

    PropertyTypeEnum(String fileExtension, PropertySourceLoader propertySourceLoader) {
        this.fileExtension = fileExtension;
        this.propertySourceLoader = propertySourceLoader;
    }
}
