package com.game.lyn.user.service;

import com.game.lyn.common.dto.ResponseDTO;
import com.game.lyn.security.JwtUtils;
import com.game.lyn.security.UserDetailsServiceImpl;
import com.game.lyn.user.entity.User;
import com.game.lyn.user.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private UserDetailsServiceImpl userDetailsService;

    // Xử lý đăng ký
    public ResponseDTO  registerUser(String username, String password , String confirmPassword) {
        // Kiểm tra xem username đã tồn tại chưa
        if (userRepository.existsByUsername(username)) {
            return new ResponseDTO("error", "Tên tài khoản đã tồn tại!", null);
        }

        // Kiểm tra password có trùng nhau không
        if (!password.equals(confirmPassword)) {
            return new ResponseDTO("error", "Mật khẩu không trùng nhau!", null);
        }

        // Tạo một user mới
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        // Lưu user vào cơ sở dữ liệu
        userRepository.save(user);

        // Lấy thông tin người dùng
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Tạo JWT token
        String jwtToken = jwtUtils.generateToken(username, userDetails.getAuthorities());

        return new ResponseDTO("success", "Bạn đã đăng kí thành công!", jwtToken);
    }

    // Xử lý đăng nhập
    public ResponseDTO  loginUser(String username, String password) {
        // Xác thực người dùng
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        // Thiết lập SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Lấy thông tin người dùng
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Tạo JWT token
        String jwtToken = jwtUtils.generateToken(username, userDetails.getAuthorities());

        return new ResponseDTO("success", "Đăng nhập thành công!", jwtToken);
    }
}
