package com.game.lyn.service;

import com.game.lyn.common.dto.RegisterRequest;
import com.game.lyn.common.dto.ResponseDTO;
import com.game.lyn.entity.Token;
import com.game.lyn.entity.User;
import com.game.lyn.exception.CustomException;
import com.game.lyn.repository.TokenRepository;
import com.game.lyn.repository.UserRepository;
import com.game.lyn.security.JwtUtils;
import com.game.lyn.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private TokenRepository tokenRepository;  // Repository để lưu token

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // Tạo logger
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    // Xử lý đăng ký
    public ResponseDTO registerUser(RegisterRequest registerRequest) {
        String username = registerRequest.getUsername();
        String password = registerRequest.getPassword();

        try {
            // Kiểm tra xem username đã tồn tại chưa
            if (userRepository.existsByUsername(username)) {
                throw new CustomException("Tên tài khoản đã tồn tại", "Vui lòng chọn tên khác", HttpStatus.CONFLICT);
            }

            // Kiểm tra password có trùng nhau không
            if (!password.equals(registerRequest.getConfirmPassword())) {
                throw new CustomException("Mật khẩu không trùng nhau", "Mật khẩu nhập lại không khớp", HttpStatus.BAD_REQUEST);
            }

            // Tạo một user mới
            User user = new User();
            user.setUsername(username); // Tên người dùng
            user.setPassword(passwordEncoder.encode(password));

            // Lưu user vào cơ sở dữ liệu
            userRepository.save(user);

            // Lấy thông tin người dùng
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Tạo JWT token
            String jwtToken = jwtUtils.generateToken(username, userDetails.getAuthorities());
            return new ResponseDTO("success", "Bạn đã đăng kí thành công!", jwtToken);
            
        } catch (Exception ex) {
            // Ghi log lỗi không xác định và ném ngoại lệ
            ex.printStackTrace();
            logger.error("Lỗi không xác định trong quá trình đăng ký tài khoản", username, ex.getMessage());
            throw new CustomException("Đã xảy ra lỗi trong quá trình đăng ký", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Xử lý đăng nhập
    public ResponseDTO  loginUser(String username, String password) {
       try {
            // Xác thực người dùng
            Authentication authentication = authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(username, password));

            // Thiết lập SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Lấy thông tin người dùng
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            User user = (User) authentication.getPrincipal();

            // Tạo JWT token
            String jwtToken = jwtUtils.generateToken(username, userDetails.getAuthorities());

            // Lưu token vào bảng tokens
            Token token = new Token();
            token.setUser(user);
            token.setToken(jwtToken);
            token.setIssuedAt(LocalDateTime.now());
            token.setExpiresAt(LocalDateTime.now().plusHours(12));  // Ví dụ token có hạn 1 giờ
            token.setIsRevoked(false);
            
            tokenRepository.save(token);

            return new ResponseDTO("success", "Đăng nhập thành công!", jwtToken);
        } catch (UsernameNotFoundException e) {
            logger.error("Không tìm thấy tài khoản", username);
            // Ném CustomException khi không tìm thấy người dùng với status NOT_FOUND
            throw new CustomException(e.getMessage(), "Tài khoản không tồn tại!", HttpStatus.NOT_FOUND );
        } catch (Exception e) {
            logger.error("Lỗi không xác định trong quá trình đăng nhập", e.getMessage());
            // Ném CustomException khi có lỗi không xác định với status INTERNAL_SERVER_ERROR
            throw new CustomException(e.getMessage(), "Đã xảy ra lỗi khi đăng nhập!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
