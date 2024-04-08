package io.github.moyugroup.orm.jpa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

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
     * 该字段不参与序列化输出
     */
    @JsonIgnore
    @Column(nullable = false)
    Boolean isDeleted;

    /**
     * 新增时自动赋值默认值
     */
    @Override
    protected void initializeOnSave() {
        if (Objects.isNull(isDeleted)) {
            isDeleted = false;
        }
    }

}
