package com.game.lyn.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.game.lyn.entity.Token;
import com.game.lyn.repository.TokenRepository;

@Service
public class TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    // Hàm thu hồi token
    public void revokeToken(String jwt) {
        // Tìm token trong cơ sở dữ liệu
        Optional<Token> tokenOptional = tokenRepository.findByToken(jwt);
        if (tokenOptional.isPresent()) {
            // Cập nhật trạng thái token thành "đã thu hồi"
            Token token = tokenOptional.get();
            token.setIsRevoked(true);
            tokenRepository.save(token);  // Lưu lại vào cơ sở dữ liệu
        }
    }
}
