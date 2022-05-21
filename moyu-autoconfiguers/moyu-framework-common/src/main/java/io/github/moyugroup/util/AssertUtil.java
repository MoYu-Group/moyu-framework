package io.github.moyugroup.util;

import io.github.moyugroup.exception.AssertException;
import io.github.moyugroup.exception.ExceptionEnum;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;

/**
 * 参数校验工具类
 * <p>
 * Created by fanfan on 2022/05/21.
 */
public class AssertUtil {
    private AssertUtil() {
    }

    /**
     * 期待表达式值为true，否则抛出异常
     *
     * @param expression
     * @param message
     */
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new AssertException(message);
        }
    }

    /**
     * 期待表达式值为true，否则抛出异常
     *
     * @param expression
     * @param exceptionEnum
     */
    public static void isTrue(boolean expression, ExceptionEnum exceptionEnum) {
        if (!expression) {
            throw new AssertException(exceptionEnum);
        }
    }

    /**
     * 期待表达式值为false，否则抛出异常
     *
     * @param expression
     * @param message
     */
    public static void isFalse(boolean expression, String message) {
        if (expression) {
            throw new AssertException(message);
        }
    }

    /**
     * 期待表达式值为false，否则抛出异常
     *
     * @param expression
     * @param exceptionEnum
     */
    public static void isFalse(boolean expression, ExceptionEnum exceptionEnum) {
        if (expression) {
            throw new AssertException(exceptionEnum);
        }
    }

    /**
     * 期待对象为null，否则抛出异常
     *
     * @param object
     * @param message
     */
    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new AssertException(message);
        }
    }

    /**
     * 期待对象为null，否则抛出异常
     *
     * @param object
     * @param exceptionEnum
     */
    public static void isNull(Object object, ExceptionEnum exceptionEnum) {
        if (object != null) {
            throw new AssertException(exceptionEnum);
        }
    }

    /**
     * 期待对象不为null，否则抛出异常
     *
     * @param object
     * @param message
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new AssertException(message);
        }
    }

    /**
     * 期待对象不为null，否则抛出异常
     *
     * @param object
     * @param exceptionEnum
     */
    public static void notNull(Object object, ExceptionEnum exceptionEnum) {
        if (object == null) {
            throw new AssertException(exceptionEnum);
        }
    }

    /**
     * 期待字符串不为null或空字符串，否则抛出异常
     *
     * @param text
     * @param message
     */
    public static void hasText(String text, String message) {
        if (!StringUtils.hasText(text)) {
            throw new AssertException(message);
        }
    }

    /**
     * 期待字符串不为null或空字符串，否则抛出异常
     *
     * @param text
     * @param exceptionEnum
     */
    public static void hasText(String text, ExceptionEnum exceptionEnum) {
        if (!StringUtils.hasText(text)) {
            throw new AssertException(exceptionEnum);
        }
    }

    /**
     * 期待字符串包含子串，否则抛出异常
     *
     * @param textToSearch
     * @param substring
     * @param message
     */
    public static void contain(String textToSearch, String substring, String message) {
        if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) &&
                textToSearch.indexOf(substring) == -1) {
            throw new AssertException(message);
        }
    }

    /**
     * 期待字符串包含子串，否则抛出异常
     *
     * @param textToSearch
     * @param substring
     * @param exceptionEnum
     */
    public static void contain(String textToSearch, String substring, ExceptionEnum exceptionEnum) {
        if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) &&
                textToSearch.indexOf(substring) == -1) {
            throw new AssertException(exceptionEnum);
        }
    }

    /**
     * 期待集合包含指定值，否则抛出异常
     *
     * @param collection
     * @param element
     * @param message
     */
    public static <T> void contain(Collection<T> collection, T element, String message) {
        Iterator<?> iterator = (collection == null) ? null : collection.iterator();

        if (!CollectionUtils.contains(iterator, element)) {
            throw new AssertException(message);
        }
    }

    /**
     * 期待集合包含指定值，否则抛出异常
     *
     * @param collection
     * @param element
     * @param exceptionEnum
     */
    public static <T> void contain(Collection<T> collection, T element, ExceptionEnum exceptionEnum) {
        Iterator<?> iterator = (collection == null) ? null : collection.iterator();

        if (!CollectionUtils.contains(iterator, element)) {
            throw new AssertException(exceptionEnum);
        }
    }

    /**
     * 期待数组不为空，否则抛出异常
     *
     * @param array
     * @param message
     */
    public static void notEmpty(Object[] array, String message) {
        if (ObjectUtils.isEmpty(array)) {
            throw new AssertException(message);
        }
    }

    /**
     * 期待数组不为空，否则抛出异常
     *
     * @param array
     * @param exceptionEnum
     */
    public static void notEmpty(Object[] array, ExceptionEnum exceptionEnum) {
        if (ObjectUtils.isEmpty(array)) {
            throw new AssertException(exceptionEnum);
        }
    }

    /**
     * 期待集合不为空，否则抛出异常
     *
     * @param collection
     * @param message
     */
    public static void notEmpty(Collection collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new AssertException(message);
        }
    }

    /**
     * 期待集合不为空，否则抛出异常
     *
     * @param collection
     * @param exceptionEnum
     */
    public static void notEmpty(Collection collection, ExceptionEnum exceptionEnum) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new AssertException(exceptionEnum);
        }
    }

    /**
     * 访问集合下标在界内，否则抛出异常
     *
     * @param collection
     * @param index
     */
    public static void withinBound(Collection collection, int index) {
        if (index < 0 || index >= collection.size()) {
            throw new AssertException(MessageFormat.format("访问集合下标{0}越界，应该在0-{1}范围内", index, collection.size() - 1));
        }
    }

    /**
     * 期待对象为指定的类型，否则抛出异常
     *
     * @param type
     * @param obj
     * @param message
     */
    public static void isInstanceOf(Class type, Object obj, String message) {
        notNull(type, "指定类型不能为空");
        if (!type.isInstance(obj)) {
            throw new AssertException(message);
        }
    }

    /**
     * 期待对象为指定的类型，否则抛出异常
     *
     * @param type
     * @param obj
     * @param exceptionEnum
     */
    public static void isInstanceOf(Class type, Object obj, ExceptionEnum exceptionEnum) {
        notNull(type, "指定类型不能为空");
        if (!type.isInstance(obj)) {
            throw new AssertException(exceptionEnum);
        }
    }

    /**
     * 期待给出类型能转换指定类型，否则抛出异常
     *
     * @param superType
     * @param subType
     * @param message
     */
    public static void isAssignable(Class superType, Class subType, String message) {
        notNull(superType, "指定类型不能为空");
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new AssertException(message);
        }
    }

    /**
     * 期待给出类型能转换指定类型，否则抛出异常
     *
     * @param superType
     * @param subType
     * @param exceptionEnum
     */
    public static void isAssignable(Class superType, Class subType, ExceptionEnum exceptionEnum) {
        notNull(superType, "指定类型不能为空");
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new AssertException(exceptionEnum);
        }
    }
}
