package com.game.lyn.admin.entity;
import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // Tên người dùng

    @Column(nullable = false)
    private String password; // Mật khẩu

    @Column(nullable = false)
    private String role; // Các role sẽ là "SUPER_ADMIN", "ADMIN", "MANAGER" hoặc "USER"

    @Column
    private String token; // Token đăng nhập 

    @Column
    private LocalDateTime lastLogin; // thời gian đăng nhập cuối cùng

    @Column
    private LocalDateTime regDate; // Ngày tham gia

    @Column
    private int fail = 0; // Số lỗi thao tác đăng nhập

    // Constructors
    public Admin() {
        this.regDate = LocalDateTime.now(); // Set default registration date
    }

    // Mã hóa mật khẩu
    public String encodePassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    // Kiểm tra mật khẩu có khớp hay không
    public boolean isPasswordValid(String rawPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(rawPassword, this.password);
    }
}
