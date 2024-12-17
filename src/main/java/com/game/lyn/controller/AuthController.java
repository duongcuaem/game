package com.game.lyn.controller;

import com.game.lyn.dto.requestDTO.LoginRequestDTO;
import com.game.lyn.dto.requestDTO.RegisterAdminRequestDTO;
import com.game.lyn.dto.requestDTO.RegisterRequestDTO;
import com.game.lyn.dto.responseDTO.AuthDTO;
import com.game.lyn.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "AuthController", description = "API xử lý thông tin người dùng")
@RequestMapping("/auth")
public class AuthController {
     @Autowired
    private AuthService authService;

    // API Đăng ký admin mới
    @Operation(summary = "Đăng ký Admin")
    @PostMapping("/admin/register")
    public AuthDTO registerAdmin(@RequestBody RegisterAdminRequestDTO registerAdminRequestDTO) {
        return authService.registerAdmin(registerAdminRequestDTO);
    }

    // API Đăng ký người dùng mới
    @Operation(summary = "Đăng ký người dùng")
    @PostMapping("/register")
    public AuthDTO register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        return authService.registerUser(registerRequestDTO);
    }

    // API Đăng nhập và trả về JWT token
    @Operation(summary = "Đăng nhập và trả về JWT token")
    @PostMapping("/login")
    public AuthDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return authService.loginUser(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());
    }
}
