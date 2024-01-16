package io.github.moyugroup.web.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 时间格式化配置类
 * <p>
 * Created by fanfan on 2024/01/16.
 */
@Data
@ConfigurationProperties(prefix = "moyu.date.format")
public class DateFormatProperties {
    /**
     * 以时间戳格式输出
     */
    private boolean writeAsTimestamps = true;
    /**
     * 以自定义格式输出
     */
    private String dateFormatPattern;
}
