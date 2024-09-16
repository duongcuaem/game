package com.game.lyn.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.game.lyn.security.MyUserDetails;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

// Đánh dấu lớp này là một Spring component để Spring có thể tự động quản lý và tiêm vào các nơi cần thiết.
@Component

// Implement interface AuditorAware với kiểu Long (id của người dùng), để Spring Data có thể sử dụng thông tin về người dùng hiện tại
public class AuditorAwareImpl implements AuditorAware<Long> {

    // Phương thức này trả về thông tin về auditor hiện tại (người dùng thực hiện hành động) dưới dạng Optional.
    @SuppressWarnings("null")
    @Override
    public Optional<Long> getCurrentAuditor() {
        // Lấy thông tin xác thực hiện tại từ SecurityContext. Đây là nơi Spring Security lưu trữ thông tin người dùng đã đăng nhập.
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Kiểm tra xem principal có phải là instance của MyUserDetails không.
        // MyUserDetails là lớp chứa thông tin của người dùng hiện tại, đã được triển khai theo giao diện UserDetails của Spring Security.
        if (principal instanceof MyUserDetails) {
            // Nếu principal là MyUserDetails, lấy ra userId từ MyUserDetails.
            MyUserDetails userDetails = (MyUserDetails) principal;
            // Trả về Optional chứa id của người dùng (dưới dạng Long).
            return Optional.of(userDetails.getId());
        } 
        // Nếu principal là String, có thể là tên người dùng (khi sử dụng anonymous authentication hoặc các loại xác thực khác).
        else if (principal instanceof String) {
            // Trường hợp này, bạn có thể xử lý thêm logic để chuyển từ tên người dùng thành userId nếu cần.
            // return Optional.empty() nếu không thể xác định id.
            return Optional.empty();
        }

        // Nếu không xác định được auditor, trả về Optional.empty().
        return Optional.empty();
    }
}
