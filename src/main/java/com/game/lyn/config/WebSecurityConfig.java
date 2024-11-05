package com.game.lyn.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.game.lyn.security.JwtRequestFilter;
import java.util.List;

@Configuration  // Đánh dấu class này là một class cấu hình của Spring
@EnableWebSecurity  // Kích hoạt Spring Security để bảo vệ ứng dụng
public class WebSecurityConfig {

    // JwtRequestFilter là một custom filter để xử lý xác thực JWT
    private final JwtRequestFilter jwtRequestFilter;

    // Inject biến clientUrl từ application.properties
    @Value("${app.client.url}")
    private String clientUrl;

    // Constructor để tiêm JwtRequestFilter
    public WebSecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    // Phương thức cấu hình bảo mật với HttpSecurity
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Thêm cấu hình CORS
            .csrf(csrf -> csrf.disable())  // Vô hiệu hóa CSRF, thường không cần thiết khi sử dụng JWT
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/ws/**").permitAll() // Cho phép truy cập công khai đến WebSocket endpoint
                .requestMatchers("/api/**").permitAll() // Cho phép truy cập công khai
                .requestMatchers("/auth/register", "/auth/login").permitAll()  // Cho phép truy cập không cần xác thực cho các endpoint này
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()  // Cho phép truy cập không cần xác thực cho Swagger UI
                .requestMatchers("/admin/super/**").hasRole("SUPER_ADMIN")  // Chỉ người dùng với vai trò SUPER_ADMIN mới có quyền truy cập endpoint này
                .requestMatchers("/admin/**").hasAnyRole("ADMIN", "SUPER_ADMIN")  // Người dùng có vai trò ADMIN hoặc SUPER_ADMIN mới có quyền truy cập
                .requestMatchers("/auth/admin/register").hasRole("ADMIN") // Chỉ cho phép quyền ADMIN
                .anyRequest().authenticated()  // Các yêu cầu khác đều yêu cầu người dùng phải xác thực
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // Cấu hình session stateless vì sử dụng JWT (không lưu trữ session trên server)
            )
            // Thêm JWT filter trước UsernamePasswordAuthenticationFilter để xử lý JWT trước khi xác thực thông thường
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();  // Trả về cấu hình HttpSecurity đã hoàn thành
    }

    // Bean để mã hóa mật khẩu sử dụng BCryptPasswordEncoder, đây là một tiêu chuẩn tốt cho bảo mật mật khẩu
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean để cung cấp AuthenticationManager, cần thiết khi muốn quản lý xác thực tùy chỉnh
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Cấu hình nguồn CORS để cho phép các yêu cầu từ các nguồn khác
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // Cho phép nguồn từ localhost:5173 (client của bạn)
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Các phương thức được phép
        configuration.setAllowedHeaders(List.of("*")); // Cho phép tất cả các header
        configuration.setAllowCredentials(true); // Cho phép gửi cookie hoặc thông tin xác thực

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Áp dụng cấu hình CORS cho tất cả các endpoint
        return source;
    }
}
