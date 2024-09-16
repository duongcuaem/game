package com.game.lyn.security;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.nio.charset.StandardCharsets;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    // Khóa bí mật để mã hóa JWT
    private final String jwtSecret = "devGameLyn2024bMdfh82JSkwrBvLZf78HsT89FnAzR3kQ";
    
    // Thời gian hết hạn của JWT (1 ngày)
    private final int jwtExpirationMs = 86400000; //  token tồn tại 1 ngày

    // Phương thức tạo khóa bí mật sử dụng chuẩn HS512 (Java 17)
    private Key getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);  // Tạo key sử dụng `Keys.hmacShaKeyFor` cho HS512
    }

    // Phương thức tạo JWT token
    public String generateToken(String username, Collection<? extends GrantedAuthority> authorities) {
        List<String> roles = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Phương thức lấy username từ JWT token
    public String getUsernameFromJwtToken(String token) {
        return Jwts.parserBuilder() // Sử dụng parserBuilder để phân tích token
                .setSigningKey(getSigningKey()) // Thiết lập khóa bí mật
                .build() // Xây dựng đối tượng parser
                .parseClaimsJws(token) // Phân tích token
                .getBody() // Lấy phần thân của token (claims)
                .getSubject(); // Lấy thông tin username từ claims
    }

    // Lấy quyền hạn (roles) từ JWT token
    @SuppressWarnings("unchecked")
    public List<String> getRolesFromJwtToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("roles", List.class);
    }

    // Phương thức xác thực JWT token
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // Cung cấp khóa bí mật
                .build() // Xây dựng đối tượng parser
                .parseClaimsJws(token); // Phân tích và xác minh chữ ký của token
            return true; // Token hợp lệ
        } catch (JwtException e) {
            System.err.println("Invalid JWT token: " + e.getMessage());
        }
        return false; // Token không hợp lệ
    }
}
