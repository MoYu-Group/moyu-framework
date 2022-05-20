package io.github.moyugroup.base.model;

/**
 * 密封类,限制了类的扩展.
 *
 * @author iPisces42
 */
public sealed interface Returned permits ErrorCode, ReturnValue {

} 