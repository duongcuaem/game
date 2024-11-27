package com.game.lyn.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.game.lyn.entity.UserInfo;
import com.game.lyn.repository.UserInfoRepository;
import com.game.lyn.utils.BeanUtilsHelper;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Service
public class UserInfoService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    // public Optional<UserInfo> getUserInfoById(Long id) {
    //     return userInfoRepository.findById(id);
    // }

    // public UserInfo saveUserInfo(UserInfo userInfo) {
    //     return userInfoRepository.save(userInfo);
    // }

    // public UserInfo updateUserInfo(Long id, UserInfo userInfoDetails) {
    //     UserInfo userInfo = userInfoRepository.findById(id).orElseThrow(() -> new RuntimeException("UserInfo not found"));
    //     userInfo.setName(userInfoDetails.getName());
    //     userInfo.setEmail(userInfoDetails.getEmail());
    //     userInfo.setAvatar(userInfoDetails.getAvatar());
    //     userInfo.setRed(userInfoDetails.getRed());
    //     userInfo.setKetSat(userInfoDetails.getKetSat());
    //     userInfo.setRedWin(userInfoDetails.getRedWin());
    //     userInfo.setRedLost(userInfoDetails.getRedLost());
    //     userInfo.setRedPlay(userInfoDetails.getRedPlay());
    //     userInfo.setTotall(userInfoDetails.getTotall());
    //     userInfo.setVip(userInfoDetails.getVip());
    //     userInfo.setLastVip(userInfoDetails.getLastVip());
    //     userInfo.setHu(userInfoDetails.getHu());
    //     userInfo.setType(userInfoDetails.isType());
    //     userInfo.setVeryphone(userInfoDetails.isVeryphone());
    //     userInfo.setVeryold(userInfoDetails.isVeryold());
    //     userInfo.setOtpFirst(userInfoDetails.isOtpFirst());
    //     userInfo.setGitCode(userInfoDetails.getGitCode());
    //     userInfo.setGitRed(userInfoDetails.getGitRed());
    //     userInfo.setGitTime(userInfoDetails.getGitTime());
    //     userInfo.setRights(userInfoDetails.getRights());
    //     return userInfoRepository.save(userInfo);
    // }

    // public void deleteUserInfo(Long id) {
    //     userInfoRepository.deleteById(id);
    // }


    /**
     * Hàm xử lý logic lấy danh sách UserInfo theo phân trang và điều kiện lọc.
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
    public Page<UserInfo> getUserInfoList(int page, int size, String name, String userName, String type, String email, LocalDate startDate, LocalDate endDate) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return userInfoRepository.findByFilters(name, userName, type, email, startDate, endDate, pageRequest);
    }

    /**
     * Hàm xử lý thêm mới UserInfo.
     * @param userInfo đối tượng userInfo cần thêm mới
     * @return bản ghi userInfo mới được thêm
     */
    public UserInfo addUserInfo(UserInfo userInfo) {
        return userInfoRepository.save(userInfo);
    }

/**
     * Hàm xử lý cập nhật UserInfo, chỉ cập nhật các trường có giá trị không null.
     * @param uid ID của userInfo cần cập nhật
     * @param userInfo thông tin userInfo mới
     * @return bản ghi userInfo đã được cập nhật
     */
    public UserInfo updateUserInfo(Long uid, UserInfo userInfo) {
        Optional<UserInfo> existingUserInfo = userInfoRepository.findById(uid);
        if (existingUserInfo.isPresent()) {
            UserInfo updatedUserInfo = existingUserInfo.get();
            
            // Sao chép các thuộc tính không null từ userInfo sang updatedUserInfo
            BeanUtils.copyProperties(userInfo, updatedUserInfo, BeanUtilsHelper.getNullPropertyNames(userInfo));

            return userInfoRepository.save(updatedUserInfo);
        } else {
            throw new RuntimeException("UserInfo with ID " + uid + " not found.");
        }
    }
}
