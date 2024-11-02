package com.game.lyn.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.game.lyn.model.Notification;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByRecipientId(String recipientId);
    List<Notification> findByChannelId(String channelId);
}