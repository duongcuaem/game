package com.game.lyn.controller;

import com.game.lyn.model.Notification;
import com.game.lyn.service.NotificationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/direct")
    public ResponseEntity<String> sendDirectNotification(@RequestBody Notification notification) {
        notificationService.sendDirectNotification(notification);
        return ResponseEntity.ok("Notification sent to " + notification.getRecipientId());
    }

    @PostMapping("/group")
    public ResponseEntity<String> sendGroupNotification(@RequestBody Notification notification) {
        notificationService.sendGroupNotification(notification);
        return ResponseEntity.ok("Notification sent to group " + notification.getChannelId());
    }
}

