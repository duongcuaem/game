package com.game.lyn.manager;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.web.reactive.socket.WebSocketSession;
import com.game.lyn.entity.Notification;
import com.game.lyn.entity.NotificationType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class NotificationManager {

    // Map để quản lý session theo ID người dùng
    private final Map<Long, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    // Map để quản lý session theo nhóm
    private final Map<Long, Map<Long, WebSocketSession>> groupSessions = new ConcurrentHashMap<>();

    // Kết nối với Redis và MongoDB
    private final ReactiveRedisTemplate<String, String> redisTemplate;
    private final ReactiveMongoTemplate mongoTemplate;

    public NotificationManager(ReactiveRedisTemplate<String, String> redisTemplate, ReactiveMongoTemplate mongoTemplate) {
        this.redisTemplate = redisTemplate;
        this.mongoTemplate = mongoTemplate;
    }

    // Thêm session theo ID người dùng
    public void addSessionForUser(Long userId, WebSocketSession session) {
        userSessions.put(userId, session);
        redisTemplate.opsForValue().set("session:" + userId, session.getId()).subscribe();
    }

    // Thêm session vào nhóm (groupId)
    public void addSessionToGroup(Long groupId, Long userId, WebSocketSession session) {
        groupSessions.computeIfAbsent(groupId, k -> new ConcurrentHashMap<>()).put(userId, session);
    }

    // Xóa session của người dùng (khi họ ngắt kết nối)
    public void removeSessionForUser(Long userId) {
        userSessions.remove(userId);
        redisTemplate.opsForValue().delete("session:" + userId).subscribe();
    }

    // Xóa session từ nhóm
    public void removeSessionFromGroup(Long groupId, Long userId) {
        Map<Long, WebSocketSession> group = groupSessions.get(groupId);
        if (group != null) {
            group.remove(userId);
            if (group.isEmpty()) {
                groupSessions.remove(groupId);
            }
        }
    }

    // Gửi thông báo đến một người dùng
    public Mono<Void> sendNotificationToUser(Long userId, Notification notification) {
        WebSocketSession session = userSessions.get(userId);
        if (session != null && session.isOpen()) {
            return session.send(Mono.just(session.textMessage(notification.getMessage()))).then();
        }
        return Mono.empty();
    }

    // Gửi thông báo đến tất cả thành viên trong nhóm
    public Mono<Void> sendNotificationToGroup(Long groupId, Notification notification) {
        Map<Long, WebSocketSession> group = groupSessions.get(groupId);
        if (group != null) {
            return Flux.fromIterable(group.values())
                    .flatMap(session -> session.send(Mono.just(session.textMessage(notification.getMessage()))))
                    .then();
        }
        return Mono.empty();
    }

    // Lưu thông báo vào MongoDB với NotificationType
    public Mono<Notification> saveNotificationToDatabase(Long userId, Long groupId, String message, NotificationType type) {
        Notification notification = new Notification(userId, groupId, message, type);
        return mongoTemplate.save(notification);
    }

    // Hàm để gửi thông báo và đồng thời lưu vào MongoDB với NotificationType
    public Mono<Void> sendAndSaveNotification(Long userId, Long groupId, String message, NotificationType type) {
        Notification notification = new Notification(userId, groupId, message, type);
        // Lưu thông báo vào MongoDB và gửi cho người dùng hoặc nhóm
        return saveNotificationToDatabase(userId, groupId, message, type)
                .then(sendNotificationToGroup(groupId, notification));
    }
}
