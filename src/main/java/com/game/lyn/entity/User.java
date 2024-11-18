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

@Entity
@Table(name = "t_user")
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Integer getBanPass() {
        return banPass;
    }

    public void setBanPass(Integer banPass) {
        this.banPass = banPass;
    }

    public Boolean getBanLogin() {
        return banLogin;
    }

    public void setBanLogin(Boolean banLogin) {
        this.banLogin = banLogin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public Integer getFail() {
        return fail;
    }

    public void setFail(Integer fail) {
        this.fail = fail;
    }

    public Boolean getLock() {
        return lock;
    }

    public void setLock(Boolean lock) {
        this.lock = lock;
    }

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