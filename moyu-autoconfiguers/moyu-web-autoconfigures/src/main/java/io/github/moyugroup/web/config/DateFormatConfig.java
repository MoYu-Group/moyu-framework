package io.github.moyugroup.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.github.moyugroup.web.config.properties.DateFormatProperties;
import io.github.moyugroup.web.config.serializer.LocalDateTimeToTimestampSerializer;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
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

    /**
     * 默认日期时间格式
     */
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 默认日期格式
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    /**
     * 默认时间格式
     */
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    @Resource
    private DateFormatProperties dateFormatProperties;

    /**
     * 时间序列化配置
     */
    @Bean
    public ObjectMapper objectMapper() {
        log.info("TimeFormatConfig.TimeFormatProperties：{}", dateFormatProperties);
        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        if (dateFormatProperties.isWriteAsTimestamps()) {
            // 以时间戳方式输出时间
            mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            module.addSerializer(LocalDateTime.class, new LocalDateTimeToTimestampSerializer());
            mapper.registerModule(module);
        } else {
            // 确保不会序列化为时间戳形式
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            // 以自定义格式输出
            String timeFormatPattern = dateFormatProperties.getDateFormatPattern();
            if (StringUtils.isBlank(timeFormatPattern)) {
                timeFormatPattern = DEFAULT_DATE_TIME_FORMAT;
            }
            // 配置 LocalDateTime 的序列化方式
            module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(timeFormatPattern)));
            // 配置 Date 的序列化方式
            module.addSerializer(Date.class, new DateSerializer(false, new SimpleDateFormat(timeFormatPattern)));
            mapper.registerModule(module);
        }
        return mapper;
    }


    /**
     * LocalDate转换器，用于转换RequestParam和PathVariable参数
     */
    @Bean
    public Converter<String, LocalDate> localDateConverter() {
        return new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(String source) {
                return LocalDate.parse(source, DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
            }
        };
    }

    /**
     * LocalDateTime转换器，用于转换RequestParam和PathVariable参数
     * 支持标准格式时间转换和时间戳类型时间转换
     */
    @Bean
    public Converter<String, LocalDateTime> localDateTimeConverter() {
        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String source) {
                try {
                    long timestamp = Long.parseLong(source);
                    return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
                } catch (NumberFormatException numberFormatException) {
                    return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT));
                }
            }
        };
    }

    /**
     * LocalTime转换器，用于转换RequestParam和PathVariable参数
     */
    @Bean
    public Converter<String, LocalTime> localTimeConverter() {
        return new Converter<String, LocalTime>() {
            @Override
            public LocalTime convert(String source) {
                return LocalTime.parse(source, DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT));
            }
        };
    }

    /**
     * Date转换器，用于转换RequestParam和PathVariable参数
     * 支持标准格式时间转换和时间戳类型时间转换
     */
    @Bean
    public Converter<String, Date> dateConverter() {
        return new Converter<String, Date>() {
            @Override
            public Date convert(String source) {
                try {
                    long time = Long.parseLong(source);
                    return new Date(time);
                } catch (NumberFormatException numberFormatException) {
                    SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT);
                    try {
                        return format.parse(source);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
    }
}
