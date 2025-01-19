package io.github.moyugroup.base.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * Json操作工具类
 * <p>
 * Created by fanfan on 2022/05/25.
 */
@Slf4j
public class JsonUtil {

    /**
     * 初始化默认的ObjectMapper实例
     */
    private final static ObjectMapper MAPPER = JsonMapper.builder()
            // 配置序列化和反序列化特性
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            // 禁用将日期序列化为时间戳数组
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            // 添加Java8时间模块
            .addModule(new JavaTimeModule())
            .build()
            // 设置默认时区为系统时区
            .setTimeZone(TimeZone.getDefault());

    /**
     * 私有构造函数，防止实例化
     */
    private JsonUtil() {
    }

    /**
     * 将对象转换为JSON字符串
     *
     * @param obj 要转换的对象
     * @return JSON字符串，如果转换失败返回null
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Convert object to JSON failed: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 将JSON字符串转换为对象
     *
     * @param json  JSON字符串
     * @param clazz 目标类型
     * @return 转换后的对象，如果转换失败返回null
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T) json : MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("Parse JSON to object failed: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 将JSON字符串转换为复杂类型对象
     *
     * @param json          JSON字符串
     * @param typeReference 类型引用
     * @return 转换后的对象，如果转换失败返回null
     */
    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return MAPPER.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            log.error("Parse JSON to complex object failed: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 将JSON字符串转换为对象列表
     *
     * @param json  JSON字符串
     * @param clazz 列表元素类型
     * @return 对象列表，如果转换失败返回null
     */
    public static <T> List<T> toList(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, clazz);
            return MAPPER.readValue(json, javaType);
        } catch (JsonProcessingException e) {
            log.error("Parse JSON to list failed: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 将JSON字符串转换为JsonNode对象
     *
     * @param json JSON字符串
     * @return JsonNode对象，如果转换失败返回null
     */
    public static JsonNode toJsonNode(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return MAPPER.readTree(json);
        } catch (JsonProcessingException e) {
            log.error("Parse JSON to JsonNode failed: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 检查字符串是否为有效的JSON格式
     * 支持的格式: JSON对象 {} 或 JSON数组 []
     *
     * @param json 要检查的字符串
     * @return 是否为有效的JSON
     */
    public static boolean isValidJson(String json) {
        if (StringUtils.isBlank(json)) {
            return false;
        }

        // 去除首尾空白字符
        json = json.trim();

        // 只接受以 { 或 [ 开头的JSON字符串
        if (!json.startsWith("{") && !json.startsWith("[")) {
            return false;
        }

        try {
            JsonNode jsonNode = MAPPER.readTree(json);
            // 进一步验证,确保是对象或数组
            return jsonNode.isObject() || jsonNode.isArray();
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    /**
     * 对象转换
     *
     * @param fromValue   源对象
     * @param toValueType 目标类型
     * @return 转换后的对象
     */
    public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
        return MAPPER.convertValue(fromValue, toValueType);
    }

    /**
     * 获取ObjectMapper实例
     *
     * @return ObjectMapper实例
     */
    public static ObjectMapper getObjectMapper() {
        return MAPPER;
    }

    /**
     * 本地方法测试
     */
    public static void main(String[] args) {
        // 序列化示例
        LocalDateTime now = LocalDateTime.now();
        String json = JsonUtil.toJson(now);
        System.out.println("LocalDateTime toJson:" + json);

        // 反序列化示例
        LocalDateTime dateTime = JsonUtil.fromJson(json, LocalDateTime.class);
        System.out.println("LocalDateTime fromJson：" + dateTime);

        TestDTO dto = new TestDTO();
        dto.setLocalDateTime(LocalDateTime.now());
        dto.setLocalDate(LocalDate.now());
        dto.setLocalTime(LocalTime.now());
        dto.setDate(new Date());

        String dtoJson = JsonUtil.toJson(dto);
        System.out.println("TestDTO toJson:" + dtoJson);
        TestDTO parsed = JsonUtil.fromJson(dtoJson, TestDTO.class);
        System.out.println("TestDTO fromJson:" + parsed);

        // 列表转换
        List<TestDTO> list = Arrays.asList(dto, dto);
        String listJson = JsonUtil.toJson(list);
        System.out.println("List<TestDTO> toJson:" + listJson);
        List<TestDTO> parsedList = JsonUtil.toList(listJson, TestDTO.class);
        System.out.println("List<TestDTO> toList:" + parsedList);

        List<TestDTO> stringList = JsonUtil.fromJson(listJson, new TypeReference<List<TestDTO>>() {
        });
        System.out.println("List<TestDTO> fromJson:" + stringList);

        // 检查JSON有效性
        boolean isValid1 = JsonUtil.isValidJson(dtoJson);
        System.out.println("jsonStr:" + dtoJson + " Is valid JSON: " + isValid1);
        boolean isValid2 = JsonUtil.isValidJson(listJson);
        System.out.println("jsonStr:" + listJson + " Is valid JSON: " + isValid2);
        boolean isValid3 = JsonUtil.isValidJson("123");
        System.out.println("jsonStr:" + "123" + " Is valid JSON: " + isValid3);

        // 对象转map
        Map<String, Object> map = JsonUtil.convertValue(parsed, Map.class);
        System.out.println("convertMap:" + toJson(map));
        TestDTO testDTO = JsonUtil.convertValue(map, TestDTO.class);
        System.out.println("convertBean:" + testDTO);
    }

    /**
     * 复杂对象示例
     */
    @Data
    private static class TestDTO {
        private LocalDateTime localDateTime;
        private LocalDate localDate;
        private LocalTime localTime;
        private Date date;
    }

}
