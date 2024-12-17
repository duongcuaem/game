package com.game.lyn.entity;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Data
@Setter
@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_user")
@Builder
public class User extends BaseEntity{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;     // Tên đăng nhập của người dùng, phải là duy nhất và không được phép null.

    @Column(nullable = false)
    private String password;     // Mật khẩu của người dùng, không được phép null.

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();  // Quyền Người dùng

    private Integer banPass = 0;
    // Số lần nhập sai mật khẩu của người dùng, mặc định là 0. 
    // Có thể sử dụng để giới hạn số lần nhập sai mật khẩu trước khi tài khoản bị khóa.
    
    private Boolean banLogin = false;    // Trạng thái khóa đăng nhập. Nếu giá trị là true, người dùng sẽ không thể đăng nhập
    private String token;     // Token đăng nhập của người dùng, có thể được sử dụng cho xác thực phiên đăng nhập.
    private String lastDate;     // Ngày cuối cùng mà người dùng thực hiện một hành động quan trọng (ví dụ: đăng nhập, thay đổi mật khẩu).
    private String lastLogin;     // Thời điểm mà người dùng đăng nhập lần cuối cùng.
    private Date regDate;     // Ngày đăng ký tài khoản của người dùng.
    private Integer fail = 0;     // Số lần đăng nhập thất bại. Dùng để theo dõi các lần nhập sai mật khẩu hoặc tài khoản bị đăng nhập thất bại.
    private Boolean lock = false;    // Trạng thái khóa rút tiền tài khoản. Nếu là true, tài khoản người dùng sẽ không thể rút tiền

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