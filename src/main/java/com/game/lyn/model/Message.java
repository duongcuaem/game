package com.game.lyn.model;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "messages")
public class Message {
    @Id
    private String id;

    private String senderId;
    private String recipientId; // null nếu là tin nhắn nhóm
    private String channelId;    // ID của kênh nhóm
    private String content;
    private LocalDateTime timestamp;
    
    public Message() {
    }

    public Message(String id, String senderId, String recipientId, String channelId, String content, LocalDateTime timestamp) {
        this.id = id;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.channelId = channelId;
        this.content = content;
        this.timestamp = timestamp;
    }
}
