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
        Map<String, WebSocketHandler> map = Map.of("/ws/notifications", handler);
        return new SimpleUrlHandlerMapping(map, -1);
    }
}
