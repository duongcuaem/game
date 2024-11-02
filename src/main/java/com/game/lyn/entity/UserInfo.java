package com.game.lyn.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;


@Entity
@Table(name = "user_info")
public class UserInfo extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Thay thế cho AutoIncrement
    private Long UID;  // Unique ID tự động tăng

    @Column(nullable = false, unique = true)
    private String id;  // ID đăng nhập

    @Column(nullable = false, unique = true)
    private String name;  // Tên nhân vật

    @Column(nullable = true)
    private String avatar = "0";  // Avatar, giá trị mặc định

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "joined_on", nullable = false)
    private Date joinedOn = new Date();  // Ngày tham gia

    @Column(nullable = true)
    private String email = "";  // Email

    @Column(nullable = true)
    private String cmt = "";  // CMT (Chứng minh thư)

    @Embedded
    private Security security = new Security();  // Bảo mật đăng nhập

    @Column(nullable = false)
    private Long red = 0L;  // RED

    @Column(nullable = false)
    private Long ketSat = 0L;  // RED trong két sắt

    @Column(nullable = false)
    private Long redWin = 0L;  // Tổng RED thắng

    @Column(nullable = false)
    private Long redLost = 0L;  // Tổng RED thua

    @Column(nullable = false)
    private Long redPlay = 0L;  // Tổng RED đã chơi

    @Column(nullable = false)
    private Long totall = 0L;  // Tổng thắng trừ thua

    @Column(nullable = false)
    private int vip = 0;  // Tổng vip tích luỹ

    @Column(nullable = false)
    private Long lastVip = 0L;  // Cập nhật lần đổi thưởng cuối

    @Column(nullable = false)
    private int hu = 0;  // Số lần Nổ Hũ Red

    @Column(nullable = false)
    private boolean type = false;  // Bot = true | User = false

    @Column(nullable = false)
    private boolean veryphone = false;  // Trạng thái xác thực điện thoại

    @Column(nullable = false)
    private boolean veryold = false;  // Đã từng xác thực

    @Column(nullable = false)
    private boolean otpFirst = false;  // Lần đầu lấy OTP

    @Column(nullable = false)
    private int gitCode = 0;  // Số lần nhận mã GiftCode thành công

    @Column(nullable = false)
    private int gitRed = 0;  // Tiền nhận từ GiftCode

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date gitTime;  // Ngày sử dụng GiftCode

    @Column(nullable = false)
    private int rights = 0;  // Cấp bậc

    public Long getUID() {
        return UID;
    }

    public void setUID(Long uID) {
        UID = uID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getJoinedOn() {
        return joinedOn;
    }

    public void setJoinedOn(Date joinedOn) {
        this.joinedOn = joinedOn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCmt() {
        return cmt;
    }

    public void setCmt(String cmt) {
        this.cmt = cmt;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public Long getRed() {
        return red;
    }

    public void setRed(Long red) {
        this.red = red;
    }

    public Long getKetSat() {
        return ketSat;
    }

    public void setKetSat(Long ketSat) {
        this.ketSat = ketSat;
    }

    public Long getRedWin() {
        return redWin;
    }

    public void setRedWin(Long redWin) {
        this.redWin = redWin;
    }

    public Long getRedLost() {
        return redLost;
    }

    public void setRedLost(Long redLost) {
        this.redLost = redLost;
    }

    public Long getRedPlay() {
        return redPlay;
    }

    public void setRedPlay(Long redPlay) {
        this.redPlay = redPlay;
    }

    public Long getTotall() {
        return totall;
    }

    public void setTotall(Long totall) {
        this.totall = totall;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public Long getLastVip() {
        return lastVip;
    }

    public void setLastVip(Long lastVip) {
        this.lastVip = lastVip;
    }

    public int getHu() {
        return hu;
    }

    public void setHu(int hu) {
        this.hu = hu;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public boolean isVeryphone() {
        return veryphone;
    }

    public void setVeryphone(boolean veryphone) {
        this.veryphone = veryphone;
    }

    public boolean isVeryold() {
        return veryold;
    }

    public void setVeryold(boolean veryold) {
        this.veryold = veryold;
    }

    public boolean isOtpFirst() {
        return otpFirst;
    }

    public void setOtpFirst(boolean otpFirst) {
        this.otpFirst = otpFirst;
    }

    public int getGitCode() {
        return gitCode;
    }

    public void setGitCode(int gitCode) {
        this.gitCode = gitCode;
    }

    public int getGitRed() {
        return gitRed;
    }

    public void setGitRed(int gitRed) {
        this.gitRed = gitRed;
    }

    public Date getGitTime() {
        return gitTime;
    }

    public void setGitTime(Date gitTime) {
        this.gitTime = gitTime;
    }

    public int getRights() {
        return rights;
    }

    public void setRights(int rights) {
        this.rights = rights;
    }

    @Embeddable
    public static class Security {
        @Column(name = "login", nullable = false)
        private int login = 0;  // Bảo mật đăng nhập
    }

    // Constructors
    public UserInfo() {
    }
}
