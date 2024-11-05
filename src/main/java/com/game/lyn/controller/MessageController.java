package com.game.lyn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.game.lyn.service.MessageService;
import com.game.lyn.model.Message;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/private")
    public ResponseEntity<String> sendPrivateMessage(@RequestParam String senderId, @RequestParam String recipientId, @RequestBody String content) {
        messageService.sendPrivateMessage(senderId, recipientId, content);
        return ResponseEntity.ok("Private message sent to user " + recipientId);
    }

    @PostMapping("/group")
    public ResponseEntity<String> sendGroupMessage(@RequestParam String senderId, @RequestParam String channelId, @RequestBody String content) {
        messageService.sendGroupMessage(senderId, channelId, content);
        return ResponseEntity.ok("Group message sent to channel " + channelId);
    }

    @PostMapping("/public")
    public ResponseEntity<String> sendPublicMessage(@RequestParam String senderId, @RequestBody String content) {
        messageService.sendPublicMessage(senderId, content);
        return ResponseEntity.ok("Public message sent to all");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<Message>> getMessagesByUserId(@PathVariable String userId, Pageable pageable) {
        Page<Message> messages = messageService.getMessagesByUserId(userId, pageable);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/channel/{channelId}")
    public ResponseEntity<Page<Message>> getMessagesByChannelId(@PathVariable String channelId, Pageable pageable) {
        Page<Message> messages = messageService.getMessagesByChannelId(channelId, pageable);
        return ResponseEntity.ok(messages);
    }
}
