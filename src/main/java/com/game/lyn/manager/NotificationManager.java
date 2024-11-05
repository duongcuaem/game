// package com.game.lyn.manager;

// import org.springframework.web.reactive.socket.WebSocketSession;
// import reactor.core.publisher.Mono;

// import java.util.concurrent.ConcurrentHashMap;
// import java.util.concurrent.CopyOnWriteArrayList;
// import java.util.List;
// import java.util.Map;

// public class NotificationManager {

//     // Map để quản lý session theo ID người dùng
//     private final Map<Integer, WebSocketSession> userSessions = new ConcurrentHashMap<>();

//     // Map để quản lý session theo nhóm
//     private final Map<String, List<WebSocketSession>> groupSessions = new ConcurrentHashMap<>();

//     // Thêm session theo ID người dùng
//     public void addSessionForUser(Integer userId, WebSocketSession session) {
//         userSessions.put(userId, session);
//     }

//     // Thêm session vào nhóm
//     public void addSessionToGroup(String groupName, WebSocketSession session) {
//         groupSessions.computeIfAbsent(groupName, key -> new CopyOnWriteArrayList<>()).add(session);
//     }

//     // Loại bỏ session khi ngắt kết nối WebSocket
//     public void removeSessionForUser(Integer userId) {
//         userSessions.remove(userId);
//     }

//     public void removeSessionFromGroup(String groupName, WebSocketSession session) {
//         List<WebSocketSession> sessions = groupSessions.get(groupName);
//         if (sessions != null) {
//             sessions.remove(session);
//             if (sessions.isEmpty()) {
//                 groupSessions.remove(groupName);
//             }
//         }
//     }

//     // Gửi thông báo tới một người dùng cụ thể
//     public void sendNotificationToUser(Integer userId, String message) {
//         WebSocketSession session = userSessions.get(userId);
//         if (session != null) {
//             session.send(Mono.just(session.textMessage(message))).subscribe();
//         }
//     }

//     // Gửi thông báo tới tất cả thành viên trong một nhóm
//     public void sendNotificationToGroup(String groupName, String message) {
//         List<WebSocketSession> sessions = groupSessions.get(groupName);
//         if (sessions != null) {
//             sessions.forEach(session -> session.send(Mono.just(session.textMessage(message))).subscribe());
//         }
//     }
// }
