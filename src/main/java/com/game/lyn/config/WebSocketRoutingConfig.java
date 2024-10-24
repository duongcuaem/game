package com.game.lyn.config;

import com.game.lyn.handler.NotificationWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;

import java.util.Map;

@Configuration
public class WebSocketRoutingConfig {

    // Định tuyến URL WebSocket tới NotificationWebSocketHandler
    @Bean
    public SimpleUrlHandlerMapping webSocketMapping(NotificationWebSocketHandler handler) {
        // Tạo một Map để ánh xạ URL "/ws/notifications" đến WebSocketHandler (NotificationWebSocketHandler)
        Map<String, WebSocketHandler> map = Map.of("/ws/notifications", handler);
        // SimpleUrlHandlerMapping: quản lý việc ánh xạ các URL đến các handler
        // Ưu tiên (-1) thấp nhất để tránh xung đột với các mapping khác
        return new SimpleUrlHandlerMapping(map, -1);
    }
}
