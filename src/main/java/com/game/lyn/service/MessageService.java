package com.game.lyn.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.game.lyn.model.Message;

public interface MessageService {

    void sendPrivateMessage(String senderId, String recipientId, String content);

    void sendGroupMessage(String senderId, String channelId, String content);

    void sendPublicMessage(String senderId, String content);

    Page<Message> getMessagesByUserId(String userId, Pageable pageable);

    Page<Message> getMessagesByChannelId(String channelId, Pageable pageable);
}
