package com.game.lyn.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.game.lyn.entity.Token;
import com.game.lyn.entity.User;
import com.game.lyn.repository.TokenRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
   
    @Autowired
    private TokenRepository tokenRepository;  // Repository để tìm token trong cơ sở dữ liệu

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // Lấy header Authorization từ request
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // Kiểm tra header Authorization có chứa JWT token
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // Bỏ tiền tố "Bearer "
            username = jwtUtils.getUsernameFromJwtToken(jwt); // Lấy username từ token
        }

        // Kiểm tra nếu có username và người dùng chưa được xác thực
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username); // Tải thông tin người dùng
            
            // Tìm token trong cơ sở dữ liệu
            Optional<Token> tokenOptional = tokenRepository.findByToken(jwt);
            
            if (tokenOptional.isPresent()) {
                Token token = tokenOptional.get();

                // Kiểm tra nếu token đã hết hạn
                if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
                    // Token hết hạn, cập nhật trạng thái token thành revoked và trả về 401
                    token.setIsRevoked(true);
                    tokenRepository.save(token); // Cập nhật trạng thái token trong DB
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Trả về mã lỗi 401 Unauthorized
                    response.getWriter().write("Token has expired"); // Thông báo token hết hạn
                    return; // Dừng chuỗi xử lý
                }

                // Kiểm tra tính hợp lệ của token và token chưa bị thu hồi
                if (jwtUtils.validateJwtToken(jwt) && !token.getIsRevoked()) {
                    User user = token.getUser();  // Lấy thông tin người dùng từ token

                    // Lấy vai trò của người dùng
                    List<String> roles = user.getRoles().stream()
                        .map(role -> role.getRoleKey()) 
                        .collect(Collectors.toList());

                    // Tạo danh sách authorities từ vai trò
                    List<org.springframework.security.core.GrantedAuthority> authorities = roles.stream()
                            .map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority(role))
                            .collect(Collectors.toList());

                    // Tạo đối tượng xác thực cho Spring Security
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

                    // Thiết lập thông tin chi tiết cho request
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Cập nhật SecurityContext với đối tượng xác thực
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    // Log các quyền
                    authenticationToken.getAuthorities().forEach(auth -> {
                        System.out.println("GrantedAuthority từ JWT: " + auth.getAuthority());
                    });
                }
            }
        }

        // Tiếp tục chuỗi filter nếu không có vấn đề với token
        filterChain.doFilter(request, response);
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Bỏ qua filter cho endpoint WebSocket
        return request.getServletPath().startsWith("/ws");
    }
}
