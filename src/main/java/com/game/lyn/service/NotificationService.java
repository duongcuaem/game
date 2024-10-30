package com.game.lyn.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.game.lyn.model.Notification;
import com.game.lyn.repository.NotificationRepository;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(NotificationRepository notificationRepository,
                               SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.messagingTemplate = messagingTemplate;
    }

    // Gửi thông báo trực tiếp
    public void sendDirectNotification(Notification notification) {
        notificationRepository.save(notification);
        messagingTemplate.convertAndSendToUser(notification.getRecipientId(), "/queue/notifications", notification);
    }

    // Gửi thông báo nhóm
    public void sendGroupNotification(Notification notification) {
        notificationRepository.save(notification);
        messagingTemplate.convertAndSend("/topic/" + notification.getChannelId(), notification);
    }
}
