package com.game.lyn.entity;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;

@MappedSuperclass  // Đánh dấu rằng đây là một lớp cha (superclass), các entity khác sẽ kế thừa từ nó. Nó không được ánh xạ trực tiếp vào bảng cơ sở dữ liệu.
@EntityListeners(AuditingEntityListener.class)  // Kích hoạt cơ chế Auditing (tự động điền các thông tin createdBy, updatedBy, createdAt, updatedAt).
public abstract class BaseEntity {  // Lớp trừu tượng (abstract class), sẽ được các entity khác kế thừa.

    @CreatedDate  // Chú thích rằng trường này sẽ được tự động thiết lập giá trị khi entity được tạo mới.
    @Column(updatable = false)  // Không cho phép cập nhật giá trị này sau khi entity được tạo.
    private LocalDateTime createdAt;  // Thời gian tạo của entity.

    @LastModifiedDate  // Chú thích rằng trường này sẽ được tự động cập nhật giá trị mỗi khi entity bị thay đổi.
    private LocalDateTime updatedAt;  // Thời gian cập nhật gần nhất của entity.

    @CreatedBy  // Chú thích rằng trường này sẽ lưu người dùng đã tạo entity. Spring sẽ tự động xử lý nếu sử dụng Auditing.
    @Column(updatable = false)  // Không cho phép cập nhật giá trị này sau khi entity được tạo.
    private Long createdBy;  // ID của người dùng đã tạo entity.

    @LastModifiedBy  // Chú thích rằng trường này sẽ lưu người dùng đã chỉnh sửa lần cuối entity.
    private Long updatedBy;  // ID của người dùng đã chỉnh sửa gần nhất.
}
