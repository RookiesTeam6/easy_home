package com.example.chatservice.message.controller;

import com.example.chatservice.message.dto.request.MessageRequest;
import com.example.chatservice.message.dto.response.MessageResponse;
import com.example.chatservice.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resident/chat/messages")
@RequiredArgsConstructor
@EnableMethodSecurity

public class MessageController {
    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageResponse> sendMessage(@RequestBody MessageRequest request) {
        return ResponseEntity.ok(messageService.sendMessage(request));
    }

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<Page<MessageResponse>> getRoomMessages(
            @PathVariable Long roomId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(messageService.getRoomMessages(roomId, pageable));
    }

    @PutMapping("/{messageId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long messageId) {
        messageService.markAsRead(messageId);
        return ResponseEntity.ok().build();
    }
}