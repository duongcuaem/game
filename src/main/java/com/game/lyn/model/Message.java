package com.game.lyn.model;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;

@Document(collection = "messages")
public class Message {
    @Id
    private String id;

    private String senderId;
    private String recipientId; // null nếu là tin nhắn nhóm
    private String channelId;    // ID của kênh nhóm
    private String content;
    private LocalDateTime timestamp;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

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
