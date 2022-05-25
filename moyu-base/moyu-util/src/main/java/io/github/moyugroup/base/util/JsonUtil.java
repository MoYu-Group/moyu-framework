package io.github.moyugroup.base.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.ser.key.Jsr310NullKeySerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Json操作工具类
 * <p>
 * Created by fanfan on 2022/05/25.
 */
@Slf4j
public class JsonUtil {
    private final static ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //支持key为null
        MAPPER.getSerializerProvider().setNullKeySerializer(new Jsr310NullKeySerializer());
    }

    private JsonUtil() {
    }

    /**
     * Java对象转换为对应的json字符串
     *
     * @param obj 需要转换的java对象
     * @return java对象的json字符串
     * @throws Exception
     */
    public static String getJsonString(Object obj) throws Exception {
        String jsonStr = null;
        if (obj == null) {
            //do nothing
        } else if (obj instanceof String) {
            jsonStr = (String) obj;
        } else {
            jsonStr = MAPPER.writeValueAsString(obj);
        }
        return jsonStr;
    }

    /**
     * Java对象转换为对应的json字符串
     *
     * @param obj 需要转换的java对象
     * @return java对象的json字符串
     */
    public static String getJsonStringQuietly(Object obj) {
        try {
            return getJsonString(obj);
        } catch (Exception e) {
            log.debug("exception when getJsonString", e);
            return null;
        }
    }

    /**
     * 解析得到java对象
     *
     * @param jsonStr
     * @param valueType 需要转换得到的java对象类型
     * @return
     * @throws Exception
     */
    public static <T> T getJavaObject(String jsonStr, Class<T> valueType) throws Exception {
        T object = null;
        if (jsonStr == null) {
            //do nothing
        } else if (valueType.isAssignableFrom(String.class)) {
            object = (T) jsonStr;
        } else {
            object = MAPPER.readValue(jsonStr, valueType);
        }
        return object;
    }

    /**
     * 解析得到java对象
     *
     * @param jsonStr
     * @param valueType 需要转换得到的java对象类型
     * @return
     */
    public static <T> T getJavaObjectQuietly(String jsonStr, Class<T> valueType) {
        try {
            return getJavaObject(jsonStr, valueType);
        } catch (Exception e) {
            log.debug("exception when getJavaObject", e);
            return null;
        }
    }

    /**
     * 解析得到java泛型对象
     *
     * @param jsonStr
     * @param valueTypeRef 泛型对象类型
     * @return
     * @throws Exception
     */
    public static <T> T getJavaGeneric(String jsonStr, TypeReference<T> valueTypeRef) throws Exception {
        return (jsonStr == null) ? null : MAPPER.readValue(jsonStr, valueTypeRef);
    }

    public static <T> T getJavaGenericQuietly(String jsonStr, TypeReference<T> valueTypeRef) {
        try {
            return getJavaGeneric(jsonStr, valueTypeRef);
        } catch (Exception e) {
            log.debug("exception when getJavaGeneric", e);
            return null;
        }
    }

    /**
     * json字符串 转List
     *
     * @param jsonStr
     * @param beanClass
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> parseArray(String jsonStr, Class<T> beanClass) throws Exception {
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanClass);
        return MAPPER.readValue(jsonStr, javaType);
    }

    /**
     * json字符串 转List
     *
     * @param jsonStr
     * @param beanClass
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> parseArrayQuietly(String jsonStr, Class<T> beanClass) {
        try {
            return parseArray(jsonStr, beanClass);
        } catch (Exception e) {
            log.debug("exception when parseArray", e);
            return null;
        }
    }

    /**
     * 解析得到java对象
     *
     * @param jsonStr
     * @param valueType 需要转换得到的java对象类型
     * @return
     */
    public static <T> T getJavaObjectByType(String jsonStr, Type valueType) {
        if (null == jsonStr) {
            return null;
        }
        try {
            return MAPPER.readValue(jsonStr, MAPPER.getTypeFactory().constructType(valueType));
        } catch (Exception e) {
            log.debug("exception when getJavaObjectByType", e);
            return null;
        }
    }
}
