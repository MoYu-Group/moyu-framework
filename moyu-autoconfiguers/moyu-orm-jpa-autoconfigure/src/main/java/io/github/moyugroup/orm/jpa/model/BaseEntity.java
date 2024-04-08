package io.github.moyugroup.orm.jpa.model;

import io.github.moyugroup.orm.jpa.generator.SnowflakeIdGenerator;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

/**
 * 类公共字段 实体
 * <p>
 * Created by fanfan on 2024/01/14.
 */
@Getter
@Setter
@MappedSuperclass
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseEntity {
    /**
     * 主键ID，使用雪花ID
     */
    @Id
    @GeneratedValue(generator = "snowflakeId")
    @GenericGenerator(name = "snowflakeId", type = SnowflakeIdGenerator.class)
    Long id;
    /**
     * 创建人
     */
    @Column(length = 128)
    String creator;
    /**
     * 修改人
     */
    @Column(length = 128)
    String modifier;
    /**
     * 创建时间，默认为当前时间
     */
    @CreatedDate
    @Column(nullable = false, updatable = false)
    LocalDateTime createTime;
    /**
     * 修改时间，默认为当前时间
     */
    @LastModifiedDate
    @Column(nullable = false)
    LocalDateTime updateTime;

    @PrePersist
    public void prePersist() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        initializeOnSave();
    }

    @PreUpdate
    public void preUpdate() {
        updateTime = LocalDateTime.now();
    }

    /**
     * 新增时初始化的字段，可被子类重写
     */
    protected void initializeOnSave() {

    }

}
