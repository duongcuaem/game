package com.game.lyn.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.game.lyn.model.Message;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findByRecipientId(String recipientId); // Lấy tin nhắn cá nhân
    List<Message> findByChannelId(String channelId);     // Lấy tin nhắn nhóm
}
