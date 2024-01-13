package io.github.moyugroup.spring.data.jpa.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * 逻辑删除字段 实体
 * <p>
 * Created by fanfan on 2024/01/14.
 */
@Getter
@Setter
@MappedSuperclass
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeletableEntity extends BaseEntity {
    /**
     * 是否删除标志，默认为false
     */
    @Column(nullable = false)
    Boolean isDeleted = false;
}
