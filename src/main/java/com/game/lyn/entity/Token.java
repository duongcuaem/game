package com.game.lyn.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "t_token")
public class Token extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // ID của token, tự động tăng

    @ManyToOne  // Liên kết với bảng User
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // Người dùng sở hữu token

    @Column(nullable = false)
    private String token;  // Token JWT

    @Column(nullable = false)
    private LocalDateTime issuedAt;  // Thời điểm token được phát hành

    @Column(nullable = false)
    private LocalDateTime expiresAt;  // Thời điểm token hết hạn

    @Column(nullable = false)
    private Boolean isRevoked = false;  // Trạng thái token: true nếu bị thu hồi, mặc định là false

    // Getters và Setters
}
