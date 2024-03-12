package io.github.moyugroup.logback.converter;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * 敏感字段屏蔽器
 * <p>
 * Created by fanfan on 2024/03/13.
 */
public class MaskingPatternConverter extends ClassicConverter {
    @Override
    public String convert(ILoggingEvent event) {
        String message = event.getFormattedMessage();
        // 替换敏感信息逻辑，比如屏蔽密码
        return maskSensitiveInfo(message);
    }

    private String maskSensitiveInfo(String message) {
        // 假设敏感信息是以"password"字段表示的
        // 这里简单地使用正则替换，实际使用时可能需要根据具体情况定制
        return message.replaceAll("\"password\":\"[^\"]*\"", "\"password\":\"******\"");
    }
}