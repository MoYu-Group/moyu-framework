package io.github.moyugroup.base.util;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.beans.BeanUtils;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * bean转换工具类
 * <p>
 * Created by fanfan on 2022/05/25.
 */
public class BeanUtil extends BeanUtils {

    /**
     * 避免重复创建DozerMapper消耗资源.
     */
    public static Mapper dozer = DozerBeanMapperBuilder.create().build();

    /**
     * 转换对象的类型
     *
     * @param source
     * @param destinationClass
     * @param <T>
     * @return
     */
    public static <T> T map(Object source, Class<T> destinationClass) {
        if (source == null) {
            return null;
        }

        return dozer.map(source, destinationClass);
    }

    /**
     * 转换List中对象的类型.
     */
    public static <T> List<T> mapList(Collection sourceList, Class<T> destinationClass) {
        if (sourceList == null) {
            return null;
        }

        List<T> destinationList = Lists.newArrayList();
        for (Object sourceObject : sourceList) {
            T destinationObject = dozer.map(sourceObject, destinationClass);
            destinationList.add(destinationObject);
        }
        return destinationList;
    }

    /**
     * 转换Set中对象的类型.
     */
    public static <T> Set<T> mapSet(Collection sourceList, Class<T> destinationClass) {
        if (sourceList == null) {
            return null;
        }

        Set<T> destinationList = Sets.newHashSet();
        for (Object sourceObject : sourceList) {
            T destinationObject = dozer.map(sourceObject, destinationClass);
            destinationList.add(destinationObject);
        }
        return destinationList;
    }

    /**
     * 将对象A的值拷贝到对象B中.
     */
    public static void copy(Object source, Object destinationObject) {
        if (source != null) {
            dozer.map(source, destinationObject);
        }
    }
}
