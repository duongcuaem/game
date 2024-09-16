package com.game.lyn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import com.game.lyn.utils.AuditorAwareImpl;

// Đánh dấu đây là lớp cấu hình của Spring
@Configuration

// Kích hoạt tính năng Auditing trong JPA, giúp tự động điền thông tin auditor (người tạo/người sửa) vào các trường được đánh dấu.
// 'auditorAwareRef' tham chiếu đến bean được dùng để xác định auditor hiện tại.
@EnableJpaAuditing(auditorAwareRef = "auditorAware")

public class JpaConfig {

    // Định nghĩa một bean cho AuditorAwareImpl. 
    // AuditorAware là một interface dùng để lấy thông tin người dùng hiện tại và được sử dụng trong auditing (khi tạo/sửa bản ghi).
    // Spring sẽ tự động sử dụng bean này để xác định ai là người thực hiện hành động tạo/sửa dữ liệu.
    @Bean
    public AuditorAwareImpl auditorAware() {
        return new AuditorAwareImpl(); // Trả về một instance của AuditorAwareImpl, lớp này sẽ cung cấp thông tin người dùng hiện tại.
    }
}
