package io.github.moyugroup.web.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 时间格式化工具类
 * <p>
 * Created by fanfan on 2024/01/17.
 */
public class DateFormatUtil {

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

    /**
     * Date 时间解析
     * 支持标准格式时间和时间戳格式时间的解析
     *
     * @param dateStr
     * @return
     */
    public static Date parseDate(String dateStr) {
        try {
            long time = Long.parseLong(dateStr);
            return new Date(time);
        } catch (NumberFormatException numberFormatException) {
            SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT);
            try {
                return format.parse(dateStr);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * LocalDateTime 时间解析
     * 支持标准格式时间和时间戳格式时间的解析
     *
     * @param dateStr
     * @return
     */
    public static LocalDateTime parseLocalDateTime(String dateStr) {
        try {
            long timestamp = Long.parseLong(dateStr);
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        } catch (NumberFormatException numberFormatException) {
            return LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT));
        }
    }
}
