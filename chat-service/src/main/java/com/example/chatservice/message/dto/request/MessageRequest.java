package com.example.chatservice.message.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessageRequest {
    private Long chatRoomId;
    private String roomName;
    private Long senderPk;
    private String content;
}