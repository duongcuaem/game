package com.game.lyn.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.game.lyn.model.Message;
import com.game.lyn.service.MessagingService;

@RestController
@RequestMapping("/api/messages")
public class MessagingController {

    private final MessagingService messagingService;

    public MessagingController(MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    @PostMapping("/direct")
    public ResponseEntity<String> sendDirectMessage(@RequestBody Message message) {
        messagingService.sendDirectMessage(message);
        return ResponseEntity.ok("Message sent to " + message.getRecipientId());
    }

    // API để gửi tin nhắn broadcast
    @PostMapping("/broadcast")
    public ResponseEntity<String> sendBroadcastMessage(@RequestBody Message message) {
        messagingService.sendBroadcastMessage(message);
        return ResponseEntity.ok("Broadcast message sent");
    }

    @PostMapping("/group")
    public ResponseEntity<String> sendGroupMessage(@RequestBody Message message) {
        messagingService.sendGroupMessage(message);
        return ResponseEntity.ok("Message sent to group " + message.getChannelId());
    }
}
