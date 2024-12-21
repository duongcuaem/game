package com.game.lyn.service.impl;


import com.game.lyn.entity.Role;
import com.game.lyn.entity.Token;
import com.game.lyn.entity.User;
import com.game.lyn.exception.CustomException;
import com.game.lyn.repository.RoleRepository;
import com.game.lyn.repository.TokenRepository;
import com.game.lyn.repository.UserRepository;
import com.game.lyn.security.JwtUtils;
import com.game.lyn.security.MyUserDetails;
import com.game.lyn.security.UserDetailsServiceImpl;
import com.game.lyn.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import com.game.lyn.dto.requestDTO.RegisterAdminRequestDTO;
import com.game.lyn.dto.requestDTO.RegisterRequestDTO;
import com.game.lyn.dto.responseDTO.AuthDTO;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public AuthDTO registerAdmin(RegisterAdminRequestDTO registerAdminRequestDTO) {
        String username = registerAdminRequestDTO.getUsername();
        String password = registerAdminRequestDTO.getPassword();
        Long roleId = registerAdminRequestDTO.getRoleId();

        try {
            if (userRepository.existsByUsername(username)) {
                throw new CustomException("Tên tài khoản đã tồn tại", "Vui lòng chọn tên khác", HttpStatus.CONFLICT);
            }

            if (!password.equals(registerAdminRequestDTO.getConfirmPassword())) {
                throw new CustomException("Mật khẩu không trùng nhau", "Mật khẩu nhập lại không khớp", HttpStatus.BAD_REQUEST);
            }

            Role role = null;
            if (roleId != null) {
                role = roleRepository.findById(roleId).orElseThrow(() ->
                        new CustomException("Vai trò không tồn tại", "Vui lòng chọn vai trò hợp lệ", HttpStatus.NOT_FOUND)
                );
            }

            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));

            if (role != null) {
                user.getRoles().add(role);
            }

            userRepository.save(user);

            return new AuthDTO("success", "Bạn đã đăng kí thành công!", null);
        } catch (Exception ex) {
            logger.error("Lỗi trong quá trình đăng ký admin: {}", ex.getMessage());
            throw new CustomException("Đã xảy ra lỗi trong quá trình đăng ký", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public AuthDTO registerUser(RegisterRequestDTO registerRequest) {
        String username = registerRequest.getUsername();
        String password = registerRequest.getPassword();

        try {
            if (userRepository.existsByUsername(username)) {
                throw new CustomException("Tên tài khoản đã tồn tại", "Vui lòng chọn tên khác", HttpStatus.CONFLICT);
            }

            if (!password.equals(registerRequest.getConfirmPassword())) {
                throw new CustomException("Mật khẩu không trùng nhau", "Mật khẩu nhập lại không khớp", HttpStatus.BAD_REQUEST);
            }

            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));

            userRepository.save(user);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            String jwtToken = jwtUtils.generateToken(username, userDetails.getAuthorities());

            return new AuthDTO("success", "Bạn đã đăng kí thành công!", jwtToken);
        } catch (Exception ex) {
            logger.error("Lỗi trong quá trình đăng ký người dùng: {}", ex.getMessage());
            throw new CustomException("Đã xảy ra lỗi trong quá trình đăng ký", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public AuthDTO loginUser(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            MyUserDetails userDetails = (MyUserDetails) userDetailsService.loadUserByUsername(username);
            User user = new User();
            user.setId(userDetails.getId());

            String jwtToken = jwtUtils.generateToken(username, userDetails.getAuthorities());

            Token token = new Token();
            token.setUser(user);
            token.setToken(jwtToken);
            token.setIssuedAt(LocalDateTime.now());
            token.setExpiresAt(LocalDateTime.now().plusHours(12));
            token.setIsRevoked(false);

            tokenRepository.save(token);

            // lấy thông tin Role , Tên người dùng , ảnh đại diện, tên đăng nhập


            return new AuthDTO("success", "Đăng nhập thành công!", jwtToken);
        } catch (Exception ex) {
            logger.error("Lỗi trong quá trình đăng nhập: {}", ex.getMessage());
            throw new CustomException("Đã xảy ra lỗi khi đăng nhập", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
