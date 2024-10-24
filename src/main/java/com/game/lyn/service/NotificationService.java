package com.game.lyn.service;

import com.game.lyn.entity.Notification;
import com.game.lyn.entity.NotificationType;
import com.game.lyn.manager.NotificationManager;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class NotificationService {

    private final NotificationManager notificationManager;

    public NotificationService(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

    // Gửi thông báo cho một người dùng cụ thể
    public Mono<Void> notifyUser(Long userId, String message) {
        Notification notification = new Notification(userId, null, message, NotificationType.PRIVATE_MESSAGE);
        return notificationManager.sendNotificationToUser(userId, notification)
                .then(notificationManager.saveNotificationToDatabase(userId, null, message, NotificationType.PRIVATE_MESSAGE).then());
    }

    // Gửi thông báo cho một nhóm cụ thể
    public Mono<Void> notifyGroup(Long groupId, Long senderId, String message) {
        Notification notification = new Notification(senderId, groupId, message, NotificationType.GROUP_MESSAGE);
        return notificationManager.sendNotificationToGroup(groupId, notification)
                .then(notificationManager.saveNotificationToDatabase(senderId, groupId, message, NotificationType.GROUP_MESSAGE).then());
    }
}
