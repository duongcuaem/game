package com.game.lyn.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.game.lyn.entity.UserInfo;

import java.time.LocalDate;
import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

     /**
     * Hàm truy vấn có sử dụng bộ lọc theo các điều kiện trong database
     * @param name tên của người dùng
     * @param userName tên người dùng
     * @param type loại tài khoản
     * @param email email
     * @param startDate ngày bắt đầu cho bộ lọc createdAt
     * @param endDate ngày kết thúc cho bộ lọc createdAt
     * @param pageable đối tượng phân trang
     * @return danh sách UserInfo sau khi áp dụng bộ lọc và phân trang
     */
    @Query("SELECT u FROM UserInfo u WHERE " +
            "(:name IS NULL OR u.name LIKE %:name%) AND " +
            "(:userName IS NULL OR u.userName LIKE %:userName%) AND " +
            "(:type IS NULL OR u.type = :type) AND " +
            "(:email IS NULL OR u.email LIKE %:email%) AND " +
            "(:startDate IS NULL OR u.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR u.createdAt <= :endDate)")
    Page<UserInfo> findByFilters(
            @Param("name") String name,
            @Param("userName") String userName,
            @Param("type") String type,
            @Param("email") String email,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);


    Optional<UserInfo> findByUserId(String userId);

    Optional<UserInfo> findByUserName(String username);
}
