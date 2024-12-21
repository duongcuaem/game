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
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_info")
@Builder
@Data
public class UserInfo extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Thay thế cho AutoIncrement
    private Long UID;  // Unique ID tự động tăng

    @Column(nullable = false, unique = true)
    private String userId;  // ID đăng nhập

    @Column(nullable = false, unique = true)
    private String userName;  // Tên tài khoản

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
    private short type = 1;  // User = 1 | Admin = 2 | Bot = 3 | Daily = 4

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

    @Column(nullable = false)
    private String referralCode = "";  // mã giới thiệu

    @Column(nullable = false)
    private String inviteCode = "";  // mã mời

    @Embeddable
    public static class Security {
        @Column(name = "login", nullable = false)
        private int login = 0;  // Bảo mật đăng nhập
    }
}
