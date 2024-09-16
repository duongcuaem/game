package com.game.lyn.admin.service;

import com.game.lyn.common.dto.RegisterRequest;
import com.game.lyn.common.dto.ResponseDTO;
import com.game.lyn.exception.CustomException;
import com.game.lyn.security.JwtUtils;
import com.game.lyn.security.UserDetailsServiceImpl;
import com.game.lyn.admin.entity.Admin;
import com.game.lyn.admin.repository.AdminRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AdminService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // Tạo logger
    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    // Xử lý đăng ký
    public ResponseDTO registerAdmin(RegisterRequest registerRequest) {
        String username = registerRequest.getUsername();
        String password = registerRequest.getPassword();

        try {
            // Kiểm tra xem username đã tồn tại chưa
            if (adminRepository.existsByUsername(username)) {
                throw new CustomException("Tên tài khoản đã tồn tại", "Vui lòng chọn tên khác", HttpStatus.CONFLICT);
            }

            // Kiểm tra password có trùng nhau không
            if (!password.equals(registerRequest.getConfirmPassword())) {
                throw new CustomException("Mật khẩu không trùng nhau", "Mật khẩu nhập lại không khớp", HttpStatus.BAD_REQUEST);
            }

            // Tạo một admin mới
            Admin admin = new Admin();
            admin.setUsername(username); // Tên người dùng
            admin.setPassword(passwordEncoder.encode(password));
            admin.setRole(registerRequest.getRole());

            // Lưu admin vào cơ sở dữ liệu
            adminRepository.save(admin);

            // Lấy thông tin người dùng
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Tạo JWT token
            String jwtToken = jwtUtils.generateToken(username, userDetails.getAuthorities());
            return new ResponseDTO("success", "Bạn đã đăng kí thành công!", jwtToken);
        } catch (CustomException e) {
            // Ghi log ngoại lệ custom và ném ra
            logger.error("Lỗi xử lý đăng ký tài khoản", username, e.getMessage());
            throw e; // Ném lại ngoại lệ để `GlobalExceptionHandler` xử lý
        } catch (Exception ex) {
            // Ghi log lỗi không xác định và ném ngoại lệ
            logger.error("Lỗi không xác định trong quá trình đăng ký tài khoản", username, ex.getMessage());
            throw new CustomException("Đã xảy ra lỗi trong quá trình đăng ký", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Xử lý đăng nhập
    public ResponseDTO loginAdmin(String username, String password) {
        try {
            // Xác thực người dùng
            Authentication authentication = authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(username, password));

            // Thiết lập SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Lấy thông tin người dùng
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Tạo JWT token
            String jwtToken = jwtUtils.generateToken(username, userDetails.getAuthorities());

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
