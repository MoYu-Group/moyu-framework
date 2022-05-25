package io.github.moyugroup.base.util;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.function.Function;

/**
 * 反射机制处理工具类
 * <p>
 * Created by fanfan on 2022/05/25.
 */
public class ReflectionUtil extends ReflectionUtils {

    /**
     * 获取字段值
     *
     * @param field
     * @param entity
     * @return
     * @throws IllegalAccessException
     */
    public static Object getFieldValue(Field field, Object entity) {
        return executeInAccessibleField(field,
                accessibleField -> ReflectionUtils.getField(accessibleField, entity));
    }

    /**
     * 设置字段值
     *
     * @param field
     * @param entity
     * @param fieldValue
     */
    public static void setFieldValue(Field field, Object entity, Object fieldValue) {
        executeInAccessibleField(field,
                accessibleField -> {
                    ReflectionUtils.setField(accessibleField, entity, fieldValue);
                    return null;
                });
    }

    /**
     * 确保操作的字段是可操作的
     *
     * @param field
     * @param function
     * @return
     */
    public static synchronized Object executeInAccessibleField(Field field, Function<Field, Object> function) {
        boolean isFieldAccessible = field.isAccessible();
        if (!isFieldAccessible) {
            field.setAccessible(true);
        }
        Object result = function.apply(field);
        if (!isFieldAccessible) {
            field.setAccessible(false);
        }
        return result;
    }
}
