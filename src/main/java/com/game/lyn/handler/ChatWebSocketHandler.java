// package com.game.lyn.handler;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.redis.core.RedisTemplate;
// import org.springframework.web.socket.TextMessage;
// import org.springframework.web.socket.WebSocketSession;
// import org.springframework.web.socket.handler.TextWebSocketHandler;

// import com.game.lyn.model.Message;
// import com.game.lyn.repository.MessageRepository;

// public class ChatWebSocketHandler extends TextWebSocketHandler {

//     @Autowired
//     private MessageRepository messageRepository;
    
//     @Autowired
//     private RedisTemplate<String, Object> redisTemplate;

//     @Override
//     public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//         // Lưu tin nhắn vào MongoDB
//         Message msg = new Message();
//         msg.setSender("user1"); // Bạn có thể lấy thông tin từ session
//         msg.setReceiver("user2"); // Đặt người nhận
//         msg.setContent(message.getPayload());
//         msg.setTimestamp(System.currentTimeMillis());
        
//         messageRepository.save(msg);

//         // Cập nhật Redis để lưu trạng thái chưa đọc
//         String unreadKey = "unread:" + msg.getReceiver();
//         redisTemplate.opsForValue().increment(unreadKey);

//         // Gửi tin nhắn cho tất cả clients trong chat channel
//         for (WebSocketSession s : session.getOpenSessions()) {
//             s.sendMessage(new TextMessage("Chat message: " + message.getPayload()));
//         }
//     }
// }
