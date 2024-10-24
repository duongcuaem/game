package com.game.lyn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.springframework.web.reactive.socket.server.upgrade.ReactorNettyRequestUpgradeStrategy;
import org.springframework.web.reactive.socket.server.WebSocketService;

@Configuration
public class WebSocketConfig {

    // WebSocketHandlerAdapter để xử lý các yêu cầu WebSocket
    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        // Kết hợp WebSocketHandler với WebSocketService để tạo kết nối WebSocket
        return new WebSocketHandlerAdapter(webSocketService());
    }

    // Sử dụng HandshakeWebSocketService thay vì WebSocketService để xử lý nâng cấp kết nối WebSocket
    @Bean
    public WebSocketService webSocketService() {
        // ReactorNettyRequestUpgradeStrategy: chiến lược nâng cấp từ HTTP lên WebSocket, sử dụng Netty
        return new HandshakeWebSocketService(new ReactorNettyRequestUpgradeStrategy());
    }
}
