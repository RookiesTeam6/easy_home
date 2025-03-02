package com.example.chatservice.chatroom.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import  jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

    private String roomName; // 새로 추가된 필드

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatRoomMember> members = new ArrayList<>();

    public ChatRoom(String roomName, Long userPk1, Long userPk2) {
        this.roomName = roomName;
        this.createdAt = LocalDateTime.now();
        this.members.add(new ChatRoomMember(this, userPk1));
        this.members.add(new ChatRoomMember(this, userPk2));
    }
}