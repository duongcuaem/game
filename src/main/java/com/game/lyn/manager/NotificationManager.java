package com.game.lyn.manager;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.web.reactive.socket.WebSocketSession;

import com.game.lyn.entity.Notification;

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

    // Gửi thông báo hoặc tin nhắn đến một người dùng (theo userId)
    public Mono<Void> sendNotificationToUser(Long userId, String message) {
        WebSocketSession session = userSessions.get(userId);
        if (session != null && session.isOpen()) {
            return session.send(Mono.just(session.textMessage(message))).then();
        }
        return Mono.empty();
    }

    // Gửi thông báo hoặc tin nhắn đến tất cả thành viên trong nhóm (theo groupId)
    public Mono<Void> sendNotificationToGroup(Long groupId, String message) {
        Map<Long, WebSocketSession> group = groupSessions.get(groupId);
        if (group != null) {
            return Flux.fromIterable(group.values())
                    .flatMap(session -> session.send(Mono.just(session.textMessage(message))))
                    .then();
        }
        return Mono.empty();
    }

    // Lưu thông báo hoặc tin nhắn vào MongoDB
    public Mono<Notification> saveNotificationToDatabase(Long userId, Long groupId, String message) {
        Notification log = new Notification(userId, groupId, message);
        return mongoTemplate.save(log);
    }
}
