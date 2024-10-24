package com.game.lyn.handler;

import com.game.lyn.entity.Notification;
import com.game.lyn.entity.NotificationType;
import com.game.lyn.manager.NotificationManager;
import com.game.lyn.security.JwtUtils;
import org.springframework.web.reactive.socket.CloseStatus;
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

        // Kiểm tra token có hợp lệ không
        if (token == null || !jwtTokenProvider.validateJwtToken(token)) {
            // Nếu token không hợp lệ, đóng kết nối WebSocket
            return session.close(CloseStatus.BAD_DATA.withReason("Invalid token"));
        }

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

                    // Xử lý logic tin nhắn
                    processMessage(userId, groupId, payload, session);
                })
                .doOnTerminate(() -> {
                    // Khi kết nối đóng, loại bỏ session
                    System.out.println("Session closed for user: " + userId);
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

    private void processMessage(Long userId, Long groupId, String payload, WebSocketSession session) {
        if (payload.startsWith("@user:")) {
            // Gửi tin nhắn riêng tư đến user khác
            String[] parts = payload.split(":");
            Long recipientId = Long.valueOf(parts[1]);
            String privateMessage = parts[2];

            // Tạo Notification cho tin nhắn riêng tư
            Notification notification = new Notification(userId, null, privateMessage, NotificationType.PRIVATE_MESSAGE);

            // Gửi tin nhắn tới người dùng và lưu vào MongoDB
            notificationManager.sendNotificationToUser(recipientId, notification).subscribe();
            notificationManager.saveNotificationToDatabase(userId, null, privateMessage, NotificationType.PRIVATE_MESSAGE).subscribe();
        } else if (payload.startsWith("@group:")) {
            // Gửi tin nhắn tới nhóm
            String groupMessage = payload.split(":")[1];

            // Tạo Notification cho tin nhắn nhóm
            Notification notification = new Notification(userId, groupId, groupMessage, NotificationType.GROUP_MESSAGE);

            // Gửi tin nhắn tới nhóm và lưu vào MongoDB
            notificationManager.sendNotificationToGroup(groupId, notification).subscribe();
            notificationManager.saveNotificationToDatabase(userId, groupId, groupMessage, NotificationType.GROUP_MESSAGE).subscribe();
        } else if (payload.startsWith("@joinGroup:")) {
            // Xử lý yêu cầu tham gia nhóm
            String[] parts = payload.split(":");
            Long newGroupId = Long.valueOf(parts[1]);

            // Cập nhật nhóm của người dùng
            notificationManager.removeSessionFromGroup(groupId, userId); // Xóa khỏi nhóm cũ (nếu cần)
            notificationManager.addSessionToGroup(newGroupId, userId, session); // Thêm vào nhóm mới

            System.out.println("User " + userId + " joined group " + newGroupId);

            // Tạo Notification cho hành động tham gia nhóm
            Notification notification = new Notification(userId, newGroupId, "Joined group " + newGroupId, NotificationType.JOIN_GROUP);

            // Gửi thông báo tới nhóm và lưu vào MongoDB
            session.send(Mono.just(session.textMessage("Joined group " + newGroupId))).subscribe();
            notificationManager.saveNotificationToDatabase(userId, newGroupId, "Joined group " + newGroupId, NotificationType.JOIN_GROUP).subscribe();
        }

        // Gửi phản hồi cho client
        session.send(Mono.just(session.textMessage("Message processed: " + payload))).subscribe();
    }

}
