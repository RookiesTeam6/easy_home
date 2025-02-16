package com.example.chatservice.chatroom.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateChatRoomRequest {
    private Long userPk1;
    private Long userPk2;
}