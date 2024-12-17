package com.game.lyn.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.game.lyn.model.Notification;

public interface NotificationService {

    void sendNotificationToUser(String userId, String content);

    void sendNotificationToChannel(String channelId, String content);

    void sendNotificationToAll(String content);

    Page<Notification> getNotificationsByUserId(String userId, Pageable pageable);
}
