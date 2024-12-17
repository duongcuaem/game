package com.game.lyn.service;

public interface TokenService {

    // Hàm thu hồi token
    public void revokeToken(String jwt);
}
