package com.game.lyn.handler;

import com.game.lyn.manager.NotificationManager;
import com.game.lyn.security.JwtUtils;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


import java.util.Map;

@Component
public class NotificationWebSocketHandler implements WebSocketHandler {

    private final NotificationManager notificationManager;
    private final JwtUtils jwtTokenProvider;

    public NotificationWebSocketHandler(NotificationManager notificationManager, JwtUtils jwtTokenProvider) {
        this.notificationManager = notificationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @SuppressWarnings("null")
    @Override
    public Mono<Void> handle(WebSocketSession session) {
        // Lấy JWT token từ URL khi kết nối WebSocket
        String query = session.getHandshakeInfo().getUri().getQuery();
        String token = extractToken(query); // Lấy token từ query string

        // Giải mã JWT để lấy ID người dùng và nhóm
        Map<String, Object> claims = jwtTokenProvider.getClaimsFromToken(token);
        Long userId = (Long) claims.get("userId");

        // Đoạn này sẽ lấy thông tin các nhóm mà user cần gửi thông báo bằng quan hệ gì đó,
        Long groupId  = (Long) claims.get("group");

        // Thêm session của người dùng vào Redis và nhóm
        notificationManager.addSessionForUser(userId, session);
        notificationManager.addSessionToGroup(groupId, userId, session);


        // Lắng nghe các tin nhắn từ client và phản hồi
        return session.receive() //Duongdx: chưa hiểu đoạn này rảnh cần kiểm tra lại
                .doOnNext(message -> {
                    String payload = message.getPayloadAsText();
                    System.out.println("Received message from user " + userId + ": " + payload);

                    // Xử lý tin nhắn theo yêu cầu
                    if (payload.startsWith("@user:")) {
                        // Gửi tin nhắn riêng tư đến user khác (theo userId)
                        String[] parts = payload.split(":");
                        Long recipientId = Long.valueOf(parts[1]);
                        String privateMessage = parts[2];
                        notificationManager.sendNotificationToUser(recipientId, privateMessage).subscribe();
                    } else if (payload.startsWith("@group:")) {
                        // Gửi tin nhắn đến cả nhóm (theo groupId)
                        String groupMessage = payload.split(":")[1];
                        notificationManager.sendNotificationToGroup(groupId, groupMessage).subscribe();
                    }
                    
                    // Lưu thông báo vào MongoDB
                    notificationManager.saveNotificationToDatabase(userId, groupId, payload).subscribe();

                    // Gửi phản hồi lại cho client
                    session.send(Mono.just(session.textMessage("Message processed: " + payload))).subscribe();

                })
                .doOnTerminate(() -> {
                    // Khi kết nối đóng, loại bỏ session
                    notificationManager.removeSessionForUser(userId);
                    notificationManager.removeSessionFromGroup(groupId, userId);
                })
                .then();// Đóng stream khi kết thúc
    }

    // Hàm tách token từ URL query string
    private String extractToken(String query) {
        String[] params = query.split("&");
        for (String param : params) {
            if (param.startsWith("token=")) {
                return param.substring(6);
            }
        }
        return null;
    }
}
