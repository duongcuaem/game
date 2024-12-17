package com.game.lyn.service;

import com.game.lyn.dto.requestDTO.RegisterAdminRequestDTO;
import com.game.lyn.dto.requestDTO.RegisterRequestDTO;
import com.game.lyn.dto.responseDTO.AuthDTO;

public interface AuthService {
    AuthDTO registerAdmin(RegisterAdminRequestDTO registerAdminRequestDTO);
    AuthDTO registerUser(RegisterRequestDTO registerRequestDTO);
    AuthDTO loginUser(String username, String password);
}
