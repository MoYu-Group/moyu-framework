package io.github.moyugroup.base.model;

/**
 * 返回值的记录类.
 *
 * @author iPisces42
 * @param returnValue 返回值
 * @param <T> 泛型
 */
public record ReturnValue<T>(T returnValue) implements Returned {

}
    

