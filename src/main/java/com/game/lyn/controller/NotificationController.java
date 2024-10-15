package com.game.lyn.controller;

import com.game.lyn.manager.NotificationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    private final NotificationManager notificationManager;

    public NotificationController(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

    // API để gửi thông báo tới một người dùng cụ thể dựa trên ID của họ
    @PostMapping("/send-notification-user")
    public void sendNotificationToUser(@RequestParam Long userId, @RequestParam String message) {
        notificationManager.sendNotificationToUser(userId, message); // Gửi thông báo đến userId
    }

    // API để gửi thông báo tới nhóm cụ thể
    @PostMapping("/send-notification-group")
    public void sendNotificationToGroup(@RequestParam Long group, @RequestParam String message) {
        notificationManager.sendNotificationToGroup(group, message); // Gửi thông báo đến group
    }
}

