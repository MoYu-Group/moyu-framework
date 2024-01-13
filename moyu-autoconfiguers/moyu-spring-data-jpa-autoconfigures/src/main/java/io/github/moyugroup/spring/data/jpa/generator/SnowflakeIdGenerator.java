package io.github.moyugroup.spring.data.jpa.generator;

import io.github.moyugroup.base.util.UUIDUtil;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

/**
 * 使用雪花算法生成ID
 * <p>
 * Created by fanfan on 2024/01/13.
 */
public class SnowflakeIdGenerator implements IdentifierGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) {
        return UUIDUtil.generateId();
    }
}
