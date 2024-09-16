package com.game.lyn.user.entity;
import java.util.Date;
import java.util.Set;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.game.lyn.common.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@Entity
@Table(name = "users")
public class User extends BaseEntity{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String password;
     
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
    
    private Integer banPass = 0;
    private Boolean banLogin = false;
    private String token;
    private String lastDate;
    private String lastLogin;
    private Date regDate;
    
    private Integer fail = 0;
    private Boolean lock = false;
    
    
    // Constructors
    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = encodePassword(password);
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

    // Tài khoản bị khóa
    public boolean isBanLogin() {
        return this.banLogin;
    }

}