package com.game.lyn.service.impl;

import com.game.lyn.security.JwtUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.game.lyn.entity.UserInfo;
import com.game.lyn.repository.UserInfoRepository;
import com.game.lyn.service.UserInfoService;
import com.game.lyn.utils.BeanUtilsHelper;
import com.game.lyn.dto.responseDTO.UserInfoDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private UserInfoRepository userInfoRepository;
    private UserDetailsService userDetailsService;
    private JwtUtils jwtUtils;

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
    @Override
    public Page<UserInfo> getUserInfoList(int page, int size, String name, String userName, String type, String email, LocalDate startDate, LocalDate endDate) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return userInfoRepository.findByFilters(name, userName, type, email, startDate, endDate, pageRequest);
    }

    /**
     * Hàm xử lý thêm mới UserInfo.
     * @param userInfo đối tượng userInfo cần thêm mới
     * @return bản ghi userInfo mới được thêm
     */
    @Override
    public UserInfo addUserInfo(UserInfo userInfo) {
        return userInfoRepository.save(userInfo);
    }

    /**
     * Hàm xử lý cập nhật UserInfo, chỉ cập nhật các trường có giá trị không null.
     * @param uid ID của userInfo cần cập nhật
     * @param userInfo thông tin userInfo mới
     * @return bản ghi userInfo đã được cập nhật
     */
    @Override
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

    /**
     * Hàm xử lý xóa UserInfo.
     * @param uid ID của userInfo cần xóa
     */
    @Override
    public void deleteUserInfo(Long uid) {
        Optional<UserInfo> existingUserInfo = userInfoRepository.findById(uid);
        if (existingUserInfo.isPresent()) {
            userInfoRepository.deleteById(uid);
        } else {
            throw new RuntimeException("UserInfo with ID " + uid + " not found.");
        }
    }

    /**
     * Hàm lấy UserInfo theo ID.
     * @param uid ID của userInfo cần lấy
     * @return Optional chứa UserInfo hoặc rỗng nếu không tìm thấy
     */
    @Override
    public Optional<UserInfo> getUserInfoById(Long uid) {
        return userInfoRepository.findById(uid);
    }

    /**
     * Hàm lấy UserInfo theo UserId.
     * @param userId của userInfo cần lấy
     * @return Optional chứa UserInfo hoặc rỗng nếu không tìm thấy
     */
    @Override
    public Optional<UserInfo> getUserInfoByUserId(String userId) {
        return userInfoRepository.findByUserId(userId);
    }

    /**
     * Hàm lấy UserInfo theo UserId.
     * @param username của userInfo cần lấy
     * @return Optional chứa UserInfo hoặc rỗng nếu không tìm thấynpm
     */
    @Override
    public Optional<UserInfo> getUserInfoByUsername(String username) {
        return userInfoRepository.findByUserName(username);
    }

    /**
     * Hàm lấy UserInfo theo username.
     * @param username của userInfo cần lấy
     * @return Optional chứa UserInfo hoặc rỗng nếu không tìm thấy
     */
    @Override
    public UserInfoDto getUserDtoByUsername(String username) {
        try {
            Optional<UserInfo> userInfo = userInfoRepository.findByUserName(username);

            // Lấy username từ SecurityContext
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            List<String> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            // Biến để lưu role hiện tại
            final String currentRole;

            // Kiểm tra và phân tích từng role
            if (!roles.isEmpty()) {
                currentRole = roles.stream()
                        .filter(role -> role.equalsIgnoreCase("Role_Admin") || role.equalsIgnoreCase("Role_Manager") || role.equalsIgnoreCase("Role_User"))
                        .findFirst()
                        .map(role -> {
                            if (role.equalsIgnoreCase("Role_Admin")) return "Admin";
                            else if (role.equalsIgnoreCase("Role_Manager")) return "Manager";
                            else return "User";
                        })
                        .orElse(""); // Mặc định nếu không tìm thấy role nào
            } else {
                currentRole = ""; // Mặc định nếu không có role
            }

            UserInfoDto userDto = new UserInfoDto();
            // Nếu userInfo có giá trị, gán các trường vào userDto
            if (userInfo.isPresent()) {
                userInfo.ifPresent(info -> {
                    userDto.setUserName(info.getUserName());
                    userDto.setAvatar(info.getAvatar());
                    userDto.setRole(currentRole);
                    userDto.setName(info.getName());
                });
            } else {
                // Trả về một đối tượng UserInfoDto mới nếu userInfo rỗng
                userDto.setUserName(username);
                userDto.setRole(currentRole);
            }

            return userDto;
        } catch (Exception e) {
            // Log lỗi và xử lý ngoại lệ
            logger.error("Lỗi xảy ra khi lấy thông tin người dùng: ", e);
            throw new RuntimeException("Đã xảy ra lỗi khi lấy thông tin người dùng", e);
        }
    }
}
