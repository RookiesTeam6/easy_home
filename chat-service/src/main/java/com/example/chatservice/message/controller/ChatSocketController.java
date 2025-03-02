package com.example.chatservice.message.controller;

import com.example.chatservice.message.dto.request.MessageRequest;
import com.example.chatservice.message.dto.response.MessageResponse;
import com.example.chatservice.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatSocketController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/room/{roomIdentifier}")
    public void sendMessage(@DestinationVariable String roomIdentifier, MessageRequest request) {
        // 메시지 저장
        MessageResponse response = messageService.sendMessage(request);

        // 해당 채팅방 구독자들에게 메시지 전송
        messagingTemplate.convertAndSend("/topic/chat/room/" + roomIdentifier, response);
    }
}