package com.game.lyn.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.game.lyn.model.Message;
import com.game.lyn.repository.MessageRepository;

@Service
public class MessagingService {

    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    public MessagingService(MessageRepository messageRepository, SimpMessagingTemplate messagingTemplate, RedisTemplate<String, Object> redisTemplate) {
        this.messageRepository = messageRepository;
        this.messagingTemplate = messagingTemplate;
        this.redisTemplate = redisTemplate;
    }

    // Phương thức gửi tin nhắn trực tiếp
    public void sendDirectMessage(Message message) {
        messageRepository.save(message);
        messagingTemplate.convertAndSendToUser(message.getRecipientId(), "/queue/messages", message);
    }

    // Phương thức gửi tin nhắn nhóm
    public void sendGroupMessage(Message message) {
        messageRepository.save(message);
        messagingTemplate.convertAndSend("/topic/" + message.getChannelId(), message);
    }

    // Gửi tin nhắn đến tất cả người dùng
    public void sendBroadcastMessage(Message message) {
        messagingTemplate.convertAndSend("/topic/broadcast", message);
    }

    // Lưu người dùng vào channel qua Redis
    public void addUserToChannel(String userId, String channelId) {
        redisTemplate.opsForSet().add("channel:" + channelId, userId);
    }

    // Xóa người dùng khỏi channel
    public void removeUserFromChannel(String userId, String channelId) {
        redisTemplate.opsForSet().remove("channel:" + channelId, userId);
    }
}
