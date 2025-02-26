package com.example.chatservice.chatroom.dto.response;

import com.example.chatservice.chatroom.entity.ChatRoom;
import com.example.chatservice.chatroom.entity.ChatRoomMember;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ChatRoomResponse {
    private Long chatRoomId;
    private String roomName; // 새로 추가된 필드
    private List<Long> memberPks;
    private LocalDateTime createdAt;

    public static ChatRoomResponse from(ChatRoom chatRoom) {
        return ChatRoomResponse.builder()
                .chatRoomId(chatRoom.getChatRoomId())
                .roomName(chatRoom.getRoomName()) // 새로 추가된 필드
                .memberPks(chatRoom.getMembers().stream()
                        .map(ChatRoomMember::getUserPk)
                        .collect(Collectors.toList()))
                .createdAt(chatRoom.getCreatedAt())
                .build();
    }
}