package com.game.lyn.admin.controller;

import com.game.lyn.common.dto.ApiResponse;
import com.game.lyn.common.dto.LoginRequest;
import com.game.lyn.common.dto.RegisterRequest;
import com.game.lyn.common.dto.ResponseDTO;
import com.game.lyn.admin.service.AdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AdminController {

    @Autowired
    private AdminService admimService;

    // API Đăng ký người dùng mới
    @PostMapping("/register/admin")
    public ResponseDTO  register(@RequestBody RegisterRequest registerRequest) {
        return admimService.registerAdmin(registerRequest);
    }

    // API Đăng nhập và trả về JWT token
    @PostMapping("/login/admin")
    public ApiResponse<ResponseDTO> login(@RequestBody LoginRequest loginRequest) {
        ApiResponse<ResponseDTO> apiResponse = new ApiResponse<>();
        apiResponse.setResult(admimService.loginAdmin(loginRequest.getUsername(), loginRequest.getPassword()));
        return apiResponse;
    }
}
