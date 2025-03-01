package com.example.chatservice.message.controller;

import com.example.chatservice.message.dto.request.MessageRequest;
import com.example.chatservice.message.dto.response.MessageResponse;
import com.example.chatservice.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resident/chat/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping
    public ResponseEntity<MessageResponse> sendMessage(@RequestBody MessageRequest request) {
        return ResponseEntity.ok(messageService.sendMessage(request));
    }

    @GetMapping("/rooms/{roomIdentifier}")
    public ResponseEntity<Page<MessageResponse>> getRoomMessages(
            @PathVariable String roomIdentifier,
            @PageableDefault(size = 20) Pageable pageable) {

        try {
            // 숫자인지 확인하여 roomId로 처리
            Long roomId = Long.parseLong(roomIdentifier);
            return ResponseEntity.ok(messageService.getRoomMessages(roomId, pageable));
        } catch (NumberFormatException e) {
            // 숫자가 아니면 roomName으로 처리
            return ResponseEntity.ok(messageService.getRoomMessagesByName(roomIdentifier, pageable));
        }
    }

    @PutMapping("/{messageId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long messageId) {
        messageService.markAsRead(messageId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/rooms/{roomIdentifier}")
    public ResponseEntity<MessageResponse> sendRoomMessage(
            @PathVariable String roomIdentifier,
            @RequestBody MessageRequest request) {

        // 메시지 저장 및 응답
        MessageResponse response = messageService.sendMessage(request);

        // WebSocket을 통해 실시간 메시지 전송
        messagingTemplate.convertAndSend("/topic/chat/room/" + roomIdentifier, response);

        return ResponseEntity.ok(response);
    }
}