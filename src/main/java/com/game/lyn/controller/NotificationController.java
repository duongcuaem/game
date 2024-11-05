package com.game.lyn.controller;

import com.game.lyn.model.Notification;
import com.game.lyn.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

   @Autowired
    private NotificationService notificationService;

    // Gửi thông báo đến một userId cụ thể
    @PostMapping("/user/{userId}")
    public ResponseEntity<String> sendNotificationToUser(@PathVariable String userId, @RequestBody String message) {
        notificationService.sendNotificationToUser(userId, message);
        return ResponseEntity.ok("Notification sent to user " + userId);
    }

    // Gửi thông báo đến một channel cụ thể
    @PostMapping("/channel/{channelName}")
    public ResponseEntity<String> sendNotificationToChannel(@PathVariable String channelName, @RequestBody String message) {
        notificationService.sendNotificationToChannel(channelName, message);
        return ResponseEntity.ok("Notification sent to channel " + channelName);
    }

    // Gửi thông báo đến toàn bộ hệ thống
    @PostMapping("/all")
    public ResponseEntity<String> sendNotificationToAll(@RequestBody String message) {
        notificationService.sendNotificationToAll(message);
        return ResponseEntity.ok("Notification sent to all");
    }

    // Lấy tất cả thông báo theo userId với phân trang
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<Notification>> getNotificationsByUserId(@PathVariable String userId, Pageable pageable) {
        Page<Notification> notifications = notificationService.getNotificationsByUserId(userId, pageable);
        return ResponseEntity.ok(notifications);
    }
}

