package com.game.lyn.common.base;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)  // Kích hoạt Auditing
public abstract class BaseEntity {

@CreatedDate
    @Column(updatable = false)  // Chỉ set khi tạo mới, không cho phép cập nhật
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(updatable = false)  // Chỉ set khi tạo mới
    private Long createdBy;

    @LastModifiedBy
    private Long updatedBy;

}
