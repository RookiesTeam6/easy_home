package com.example.chatservice.message.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    private Long chatRoomId;
    private Long senderPk;
    private String content;
    private LocalDateTime sentAt;
    private boolean isRead;

    @Builder
    public ChatMessage(Long chatRoomId, Long senderPk, String content) {
        this.chatRoomId = chatRoomId;
        this.senderPk = senderPk;
        this.content = content;
        this.sentAt = LocalDateTime.now();
        this.isRead = false;
    }

    public void markAsRead() {
        this.isRead = true;
    }
}