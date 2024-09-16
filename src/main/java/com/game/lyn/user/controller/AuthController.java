package com.game.lyn.user.controller;

import com.game.lyn.common.dto.LoginRequest;
import com.game.lyn.common.dto.RegisterRequest;
import com.game.lyn.common.dto.ResponseDTO;
import com.game.lyn.user.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // API Đăng ký người dùng mới
    @PostMapping("/register")
    public ResponseDTO  register(@RequestBody RegisterRequest registerRequest) {
        return authService.registerUser(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getConfirmPassword());
    }

    // API Đăng nhập và trả về JWT token
    @PostMapping("/login")
    public ResponseDTO  login(@RequestBody LoginRequest loginRequest) {
        return authService.loginUser(loginRequest.getUsername(), loginRequest.getPassword());
    }
}
