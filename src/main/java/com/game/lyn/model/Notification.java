package com.game.lyn.model;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;

@Document(collection = "notifications")
public class Notification  {
    @Id
    private String id;

    private String recipientId;  // ID người nhận, null nếu là thông báo nhóm
    private String channelId;     // ID kênh nếu là thông báo nhóm
    private String content;
    private boolean isRead;
    private LocalDateTime timestamp;
   
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Notification(String id, String recipientId, String channelId, String content, boolean isRead,
            LocalDateTime timestamp) {
        this.id = id;
        this.recipientId = recipientId;
        this.channelId = channelId;
        this.content = content;
        this.isRead = isRead;
        this.timestamp = timestamp;
    }

    public Notification(String recipientId, String channelId, String content, boolean isRead, LocalDateTime timestamp) {
        this.recipientId = recipientId;
        this.channelId = channelId;
        this.content = content;
        this.isRead = isRead;
        this.timestamp = timestamp;
    }

    public Notification() {
    }

}
