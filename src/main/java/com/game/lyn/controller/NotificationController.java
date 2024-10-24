package com.game.lyn.controller;

import com.game.lyn.entity.Notification;
import com.game.lyn.service.NotificationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // Gửi thông báo dưới dạng model Notification
    @PostMapping("/notify/user")
    public Mono<Void> notifyUser(@RequestBody Notification notification) {
        return notificationService.notifyUser(notification.getUserId(), notification.getMessage());
    }

    // Gửi thông báo đến một nhóm dưới dạng model Notification
    @PostMapping("/notify/group")
    public Mono<Void> notifyGroup(@RequestBody Notification notification) {
        return notificationService.notifyGroup(notification.getGroupId(), notification.getUserId(), notification.getMessage());
    }
}
