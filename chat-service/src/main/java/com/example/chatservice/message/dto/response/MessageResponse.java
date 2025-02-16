package com.example.chatservice.message.dto.response;

import com.example.chatservice.message.entity.ChatMessage;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MessageResponse {
    private Long messageId;
    private Long chatRoomId;
    private Long senderPk;
    private String content;
    private LocalDateTime sentAt;
    private boolean isRead;

    public static MessageResponse from(ChatMessage message) {
        return MessageResponse.builder()
                .messageId(message.getMessageId())
                .chatRoomId(message.getChatRoomId())
                .senderPk(message.getSenderPk())
                .content(message.getContent())
                .sentAt(message.getSentAt())
                .isRead(message.isRead())
                .build();
    }
}