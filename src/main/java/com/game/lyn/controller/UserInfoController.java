package com.game.lyn.controller;

import com.game.lyn.dto.responseDTO.UserInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.game.lyn.entity.UserInfo;
import com.game.lyn.service.UserInfoService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/userinfo")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;



    // //tạo info người dùng mới
    // @PostMapping
    // public UserInfo createUserInfo(@RequestBody UserInfo userInfo) {
    //     return userInfoService.saveUserInfo(userInfo);
    // }

    // //cập nhật info người dùng mới
    // @PutMapping("/{id}")
    // public ResponseEntity<UserInfo> updateUserInfo(@PathVariable Long id, @RequestBody UserInfo userInfoDetails) {
    //     return ResponseEntity.ok(userInfoService.updateUserInfo(id, userInfoDetails));
    // }

    // //xóa info người dùng
    // @DeleteMapping("/{id}")
    // public ResponseEntity<Void> deleteUserInfo(@PathVariable Long id) {
    //     userInfoService.deleteUserInfo(id);
    //     return ResponseEntity.noContent().build();
    // }

    /**
     * API để lấy  UserInfo theo username.
     *
     * @param username tên người dùng (username) để lấy
     * @return UserInfo
     */
    @GetMapping("/dto/{username}")
    public UserInfoDto getUserDtoByUsername(@PathVariable String username) {
        return userInfoService.getUserDtoByUsername(username);
    }


    /**
     * API để lấy danh sách UserInfo với phân trang và điều kiện lọc.
     * @param page số trang hiện tại
     * @param size số lượng phần tử trên mỗi trang
     * @param name tên của người dùng để lọc
     * @param userName tên người dùng (username) để lọc
     * @param type loại tài khoản để lọc
     * @param email email để lọc
     * @param startDate ngày bắt đầu cho bộ lọc createdAt
     * @param endDate ngày kết thúc cho bộ lọc createdAt
     * @return danh sách UserInfo sau khi áp dụng phân trang và lọc
     */
    @GetMapping("/list")
    public Page<UserInfo> getUserInfoList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        return userInfoService.getUserInfoList(page, size, name, userName, type, email, startDate, endDate);
    }

    /**
     * API để thêm một bản ghi mới vào bảng user_info.
     * @param userInfo đối tượng chứa thông tin userInfo để thêm mới
     * @return thông báo và trạng thái của kết quả thêm mới
     */
    @PostMapping("/add")
    public ResponseEntity<UserInfo> addUserInfo(@RequestBody UserInfo userInfo) {
        UserInfo newUserInfo = userInfoService.addUserInfo(userInfo);
        return ResponseEntity.ok(newUserInfo);
    }

    /**
     * API để cập nhật bản ghi trong bảng user_info.
     * @param uid ID của bản ghi userInfo cần cập nhật
     * @param userInfo đối tượng chứa thông tin userInfo cập nhật
     * @return thông báo và trạng thái của kết quả cập nhật
     */
    @PutMapping("/update/{uid}")
    public ResponseEntity<UserInfo> updateUserInfo(@PathVariable Long uid, @RequestBody UserInfo userInfo) {
        UserInfo updatedUserInfo = userInfoService.updateUserInfo(uid, userInfo);
        return ResponseEntity.ok(updatedUserInfo);
    }
}
