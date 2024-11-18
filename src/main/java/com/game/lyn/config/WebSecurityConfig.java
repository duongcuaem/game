package com.game.lyn.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    // Inject khóa bí mật từ application.properties hoặc biến môi trường
    @Value("${jwt.secret}")
    private String jwtSecret;

    // Cấu hình SecurityFilterChain cho Spring Security
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Cấu hình CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // Vô hiệu hóa CSRF vì chúng ta sử dụng JWT
            .csrf(csrf -> csrf.disable())
            // Cấu hình phân quyền cho các yêu cầu HTTP
            .authorizeHttpRequests(auth -> auth
                // Cho phép truy cập không cần xác thực đến các endpoint này
                .requestMatchers("/ws/**").permitAll()
                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/auth/register", "/auth/login").permitAll()
                .requestMatchers("/admin/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
                .requestMatchers("/auth/admin/register").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            // Cấu hình quản lý session là stateless vì sử dụng JWT
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            // Cấu hình để sử dụng xác thực JWT với oauth2ResourceServer
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.decoder(jwtDecoder()))
            );

        return http.build();
    }
    // Bean để giải mã và xác thực JWT
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(secretKey()).build();
    }

    // Tạo SecretKey từ khóa bí mật
    @Bean
    public SecretKey secretKey() {
        return new SecretKeySpec(jwtSecret.getBytes(), "HmacSHA256");
    }

    // Bean để mã hóa mật khẩu sử dụng BCryptPasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean để cung cấp AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Cấu hình nguồn CORS để cho phép các yêu cầu từ các nguồn khác
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Thêm các nguồn được phép truy cập (thay thế bằng URL của client của bạn)
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        // Cho phép các phương thức HTTP
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Cho phép tất cả các header
        configuration.setAllowedHeaders(List.of("*"));
        // Cho phép gửi thông tin xác thực (như cookie)
        configuration.setAllowCredentials(true);

        // Áp dụng cấu hình CORS cho tất cả các endpoint
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
