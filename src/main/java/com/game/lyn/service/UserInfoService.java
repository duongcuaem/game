package com.game.lyn.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.game.lyn.entity.UserInfo;

import java.time.LocalDate;
import java.util.Optional;

public interface UserInfoService {

    // Lấy danh sách UserInfo theo các bộ lọc và phân trang
    Page<UserInfo> getUserInfoList(int page, int size, String name, String userName, String type, String email, LocalDate startDate, LocalDate endDate);

    // Thêm mới UserInfo
    UserInfo addUserInfo(UserInfo userInfo);

    // Cập nhật UserInfo (chỉ cập nhật trường không null)
    UserInfo updateUserInfo(Long uid, UserInfo userInfo);

    // Xóa UserInfo
    void deleteUserInfo(Long uid);

    // Lấy UserInfo theo ID
    Optional<UserInfo> getUserInfoById(Long uid);

    // Lấy UserInfo theo userID
    Optional<UserInfo> getUserInfoByUserId(String userId);
}
