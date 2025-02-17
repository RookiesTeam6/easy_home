package com.example.chatservice.message.service;

import com.example.chatservice.message.dto.request.MessageRequest;
import com.example.chatservice.message.dto.response.MessageResponse;
import com.example.chatservice.message.entity.ChatMessage;
import com.example.chatservice.message.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageService {
    private final ChatMessageRepository messageRepository;

    @Transactional
    public MessageResponse sendMessage(MessageRequest request) {
        ChatMessage message = ChatMessage.builder()
                .chatRoomId(request.getChatRoomId())
                .senderPk(request.getSenderPk())
                .content(request.getContent())
                .build();
        return MessageResponse.from(messageRepository.save(message));
    }

    public Page<MessageResponse> getRoomMessages(Long roomId, Pageable pageable) {
        return messageRepository.findByChatRoomIdOrderBySentAtDesc(roomId, pageable)
                .map(MessageResponse::from);
    }

    @Transactional
    public void markAsRead(Long messageId) {
        ChatMessage message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        message.markAsRead();
    }
}