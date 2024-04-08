package io.github.moyugroup.spring.data.jpa.model;

import io.github.moyugroup.spring.data.jpa.generator.SnowflakeIdGenerator;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

/**
 * 类公共ID字段 实体
 * <p>
 * Created by fanfan on 2024/03/12.
 */
@Getter
@Setter
@MappedSuperclass
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseIdEntity {

    /**
     * 主键ID，使用雪花ID
     */
    @Id
    @GeneratedValue(generator = "snowflakeId")
    @GenericGenerator(name = "snowflakeId", type = SnowflakeIdGenerator.class)
    Long id;

}
