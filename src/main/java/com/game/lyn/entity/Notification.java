package com.game.lyn.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "notifications")
public class Notification {

    @Id
    private String id;
    private Long userId;        // ID người dùng gửi thông báo
    private Long groupId;       // ID nhóm mà thông báo thuộc về (nếu có)
    private String message;     // Nội dung thông báo
    private NotificationType type; // Loại thông báo
    private LocalDateTime timestamp;  // Thời gian gửi thông báo

    public Notification(Long userId, Long groupId, String message, NotificationType type) {
        this.userId = userId;
        this.groupId = groupId;
        this.message = message;
        this.type = type;
        this.timestamp = LocalDateTime.now();  // Ghi nhận thời gian gửi
    }
}
