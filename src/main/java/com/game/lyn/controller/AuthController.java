package com.game.lyn.controller;

import com.game.lyn.common.dto.LoginRequest;
import com.game.lyn.common.dto.RegisterAdminRequest;
import com.game.lyn.common.dto.RegisterRequest;
import com.game.lyn.common.dto.ResponseDTO;
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
    public ResponseDTO  registerAdmin(@RequestBody RegisterAdminRequest registerAdminRequest) {
        return authService.registerAdmin(registerAdminRequest);
    }

    // API Đăng ký người dùng mới
    @Operation(summary = "Đăng ký người dùng")
    @PostMapping("/register")
    public ResponseDTO  register(@RequestBody RegisterRequest registerRequest) {
        return authService.registerUser(registerRequest);
    }

    // API Đăng nhập và trả về JWT token
    @Operation(summary = "Đăng nhập và trả về JWT token")
    @PostMapping("/login")
    public ResponseDTO  login(@RequestBody LoginRequest loginRequest) {
        return authService.loginUser(loginRequest.getUsername(), loginRequest.getPassword());
    }
}
