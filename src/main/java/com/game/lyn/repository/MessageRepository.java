package com.game.lyn.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.game.lyn.model.Message;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findByRecipientId(String recipientId); // Lấy tin nhắn cá nhân
    Page<Message> findByChannelId(String channelId, Pageable pageable);     // Lấy tin nhắn nhóm
    Page<Message> findByRecipientId(String recipientId, Pageable pageable);

}
