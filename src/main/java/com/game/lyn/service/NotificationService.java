package com.game.lyn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import com.game.lyn.model.Notification;
import com.game.lyn.repository.NotificationRepository;

@Service
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private NotificationRepository notificationRepository;

    /**
     * Gửi thông báo đến một user cụ thể và lưu thông báo vào cơ sở dữ liệu.
     * 
     * @param recipientId ID của người nhận thông báo.
     * @param content     Nội dung của thông báo.
     */
    public void sendNotificationToUser(String recipientId, String content) {
        // Tạo thông báo mới
        Notification notification = new Notification(
            recipientId,    // ID người nhận
            null,           // Không phải thông báo nhóm, nên channelId là null
            content,        // Nội dung thông báo
            false,          // Đánh dấu là chưa đọc
            LocalDateTime.now() // Thời gian hiện tại
        );

        // Lưu thông báo vào cơ sở dữ liệu
        notificationRepository.save(notification);

        // Gửi thông báo đến người dùng qua WebSocket
        messagingTemplate.convertAndSendToUser(recipientId, "/queue/notification", content);
    }

    /**
     * Gửi thông báo đến một channel cụ thể và lưu thông báo vào cơ sở dữ liệu.
     * 
     * @param channelId ID của kênh.
     * @param content   Nội dung của thông báo.
     */
    public void sendNotificationToChannel(String channelId, String content) {
        // Tạo thông báo mới cho kênh
        Notification notification = new Notification(
            null,           // Không phải thông báo cá nhân, nên recipientId là null
            channelId,      // ID kênh
            content,        // Nội dung thông báo
            false,          // Đánh dấu là chưa đọc
            LocalDateTime.now() // Thời gian hiện tại
        );

        // Lưu thông báo vào cơ sở dữ liệu
        notificationRepository.save(notification);

        // Gửi thông báo đến kênh qua WebSocket
        messagingTemplate.convertAndSend("/topic/" + channelId, content);
    }

    /**
     * Gửi thông báo đến toàn hệ thống và lưu thông báo vào cơ sở dữ liệu.
     * 
     * @param content Nội dung của thông báo.
     */
    public void sendNotificationToAll(String content) {
        Notification notification = new Notification(
            null, // Không có người nhận cụ thể
            null, // ID kênh giả định cho toàn hệ thống
            content,
            false,
            LocalDateTime.now()
        );

        // Lưu thông báo vào cơ sở dữ liệu
        //notificationRepository.save(notification);

        // Gửi thông báo đến tất cả các client qua WebSocket
        messagingTemplate.convertAndSend("/topic/notification", content);
    }

    /**
     * Lấy tất cả các thông báo theo userId có phân trang.
     * 
     * @param userId   ID của người nhận thông báo.
     * @param pageable Thông tin phân trang.
     * @return Page chứa các thông báo.
     */
    public Page<Notification> getNotificationsByUserId(String userId, Pageable pageable) {
        // Lấy thông báo từ repository theo userId có phân trang
        return notificationRepository.findByRecipientId(userId, pageable);
    }
}
