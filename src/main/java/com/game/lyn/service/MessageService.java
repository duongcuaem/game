package com.game.lyn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import com.game.lyn.model.Message;
import com.game.lyn.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageRepository messageRepository;

    /**
     * Gửi tin nhắn riêng đến một user cụ thể và lưu vào cơ sở dữ liệu.
     * 
     * @param senderId    ID người gửi.
     * @param recipientId ID người nhận.
     * @param content     Nội dung của tin nhắn.
     */
    public void sendPrivateMessage(String senderId, String recipientId, String content) {
        Message message = new Message(
            senderId,
            recipientId,
            null,
            content,
            LocalDateTime.now()
        );

        messageRepository.save(message);
        messagingTemplate.convertAndSendToUser(recipientId, "/queue/message", content);
    }

    /**
     * Gửi tin nhắn đến một channel cụ thể và lưu vào cơ sở dữ liệu.
     * 
     * @param senderId  ID người gửi.
     * @param channelId ID kênh.
     * @param content   Nội dung của tin nhắn.
     */
    public void sendGroupMessage(String senderId, String channelId, String content) {
        Message message = new Message(
            senderId,
            null,
            channelId,
            content,
            LocalDateTime.now()
        );

        messageRepository.save(message);
        messagingTemplate.convertAndSend("/topic/" + channelId, content);
    }

    /**
     * Gửi tin nhắn chung đến toàn hệ thống và lưu vào cơ sở dữ liệu.
     * 
     * @param senderId ID người gửi.
     * @param content  Nội dung của tin nhắn.
     */
    public void sendPublicMessage(String senderId, String content) {
        Message message = new Message(
            senderId,
            null,
            "all",
            content,
            LocalDateTime.now()
        );

        messageRepository.save(message);
        messagingTemplate.convertAndSend("/topic/public", content);
    }

    /**
     * Lấy tất cả các tin nhắn theo userId với phân trang.
     * 
     * @param userId   ID của người nhận.
     * @param pageable Thông tin phân trang.
     * @return Page chứa các tin nhắn.
     */
    public Page<Message> getMessagesByUserId(String userId, Pageable pageable) {
        return messageRepository.findByRecipientId(userId, pageable);
    }

    /**
     * Lấy tất cả các tin nhắn theo channelId với phân trang.
     * 
     * @param channelId ID của kênh.
     * @param pageable  Thông tin phân trang.
     * @return Page chứa các tin nhắn.
     */
    public Page<Message> getMessagesByChannelId(String channelId, Pageable pageable) {
        return messageRepository.findByChannelId(channelId, pageable);
    }
}
