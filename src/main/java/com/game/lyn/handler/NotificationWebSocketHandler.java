// package com.game.lyn.handler;

// import com.game.lyn.manager.NotificationManager;
// import com.game.lyn.security.JwtUtils;
// import org.springframework.web.reactive.socket.WebSocketHandler;
// import org.springframework.web.reactive.socket.WebSocketSession;
// import org.springframework.stereotype.Component;
// import reactor.core.publisher.Mono;

// import java.util.Map;

// @Component
// public class NotificationWebSocketHandler implements WebSocketHandler {

//     private final NotificationManager notificationManager;
//     private final JwtUtils jwtTokenProvider;

//     public NotificationWebSocketHandler(NotificationManager notificationManager, JwtUtils jwtTokenProvider) {
//         this.notificationManager = notificationManager;
//         this.jwtTokenProvider = jwtTokenProvider;
//     }

//     @Override
//     public Mono<Void> handle(WebSocketSession session) {
//         // Lấy JWT token từ URL khi kết nối WebSocket
//         String query = session.getHandshakeInfo().getUri().getQuery();
//         String token = extractToken(query); // Lấy token từ query string

//         // Giải mã JWT để lấy ID người dùng và nhóm
//         Map<String, Object> claims = jwtTokenProvider.getClaimsFromToken(token);
//         Integer userId = (Integer) claims.get("userId");

//         // Đoạn này sẽ lấy thông tin các nhóm mà user cần gửi thông báo bằng quan hệ gì đó,
//         String groupName = (String) claims.get("group");

//         // Thêm session vào map với key là userId
//         notificationManager.addSessionForUser(userId, session);

//         // Thêm session vào nhóm
//         notificationManager.addSessionToGroup(groupName, session);

//         return session.receive() //Duongdx: chưa hiểu đoạn này rảnh cần kiểm tra lại
//                 .doOnNext(message -> {
//                     // Xử lý các tin nhắn khác (nếu có)
//                     System.out.println("Received message: " + message.getPayloadAsText());
//                 })
//                 .doOnTerminate(() -> {
//                     // Khi kết nối đóng, loại bỏ session
//                     notificationManager.removeSessionForUser(userId);
//                     notificationManager.removeSessionFromGroup(groupName, session);
//                 })
//                 .then();
//     }

//     // Hàm tách token từ URL query string
//     private String extractToken(String query) {
//         String[] params = query.split("&");
//         for (String param : params) {
//             if (param.startsWith("token=")) {
//                 return param.substring(6);
//             }
//         }
//         return null;
//     }
// }
