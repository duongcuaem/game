package com.game.lyn.model;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "notifications")
public class Notification  {
    @Id
    private String id;

    private String recipientId;  // ID người nhận, null nếu là thông báo nhóm
    private String channelId;     // ID kênh nếu là thông báo nhóm
    private String content;
    private boolean isRead;
    private LocalDateTime timestamp;
   
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
