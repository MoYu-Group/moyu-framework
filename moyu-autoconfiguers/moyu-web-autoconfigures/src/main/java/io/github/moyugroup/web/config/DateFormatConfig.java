package io.github.moyugroup.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.github.moyugroup.web.config.deserializer.DateDeserializer;
import io.github.moyugroup.web.config.deserializer.LocalDateTimeDeserializer;
import io.github.moyugroup.web.config.properties.DateFormatProperties;
import io.github.moyugroup.web.config.serializer.LocalDateTimeToTimestampSerializer;
import io.github.moyugroup.web.util.DateFormatUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 时间格式化配置
 * <p>
 * Created by fanfan on 2024/01/16.
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(DateFormatProperties.class)
public class DateFormatConfig {


    @Resource
    private DateFormatProperties dateFormatProperties;

    /**
     * 时间序列化配置
     */
    @Bean
    public ObjectMapper objectMapper() {
        log.info("init DateFormatConfig with {}", dateFormatProperties);
        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        if (dateFormatProperties.isWriteAsTimestamps()) {
            // 以时间戳方式输出时间
            mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            module.addSerializer(LocalDateTime.class, new LocalDateTimeToTimestampSerializer());

        } else {
            // 确保不会序列化为时间戳形式
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            // 以自定义格式输出
            String timeFormatPattern = dateFormatProperties.getDateFormatPattern();
            if (StringUtils.isBlank(timeFormatPattern)) {
                timeFormatPattern = DateFormatUtil.DEFAULT_DATE_TIME_FORMAT;
            }
            // 配置 LocalDateTime 的序列化方式
            module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(timeFormatPattern)));
            // 配置 Date 的序列化方式
            module.addSerializer(Date.class, new DateSerializer(false, new SimpleDateFormat(timeFormatPattern)));
        }
        // 时间反序列化配置
        module.addDeserializer(Date.class, new DateDeserializer());
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        // 注册自定义时间模块
        mapper.registerModule(module);
        return mapper;
    }

    /**
     * Date 解析器，用于解析 RequestParam 和 PathVariable 时间参数
     * 支持标准格式时间转换和时间戳类型时间转换
     */
    @Bean
    public Converter<String, Date> dateConverter() {
        return new Converter<String, Date>() {
            @Override
            public Date convert(String source) {
                return DateFormatUtil.parseDate(source);
            }
        };
    }

    /**
     * LocalDate 解析器，用于解析 RequestParam 和 PathVariable 时间参数
     */
    @Bean
    public Converter<String, LocalDate> localDateConverter() {
        return new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(String source) {
                return LocalDate.parse(source, DateTimeFormatter.ofPattern(DateFormatUtil.DEFAULT_DATE_FORMAT));
            }
        };
    }

    /**
     * LocalDateTime 解析器，用于解析 RequestParam 和 PathVariable 时间参数
     * 支持标准格式时间转换和时间戳类型时间转换
     */
    @Bean
    public Converter<String, LocalDateTime> localDateTimeConverter() {
        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String source) {
                return DateFormatUtil.parseLocalDateTime(source);
            }
        };
    }

    /**
     * LocalTime 解析器，用于解析 RequestParam 和 PathVariable 时间参数
     */
    @Bean
    public Converter<String, LocalTime> localTimeConverter() {
        return new Converter<String, LocalTime>() {
            @Override
            public LocalTime convert(String source) {
                return LocalTime.parse(source, DateTimeFormatter.ofPattern(DateFormatUtil.DEFAULT_TIME_FORMAT));
            }
        };
    }

}
