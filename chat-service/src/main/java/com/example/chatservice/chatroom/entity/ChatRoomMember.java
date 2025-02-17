package com.example.chatservice.chatroom.entity;

import com.example.chatservice.chatroom.entity.ChatRoom;
import com.example.chatservice.chatroom.entity.ChatRoomMemberId;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import  jakarta.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomMember {
    @EmbeddedId
    private ChatRoomMemberId id;

    @MapsId("chatRoomId")
    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    private boolean isActive;

    public ChatRoomMember(ChatRoom chatRoom, Long userPk) {
        this.id = new ChatRoomMemberId(chatRoom.getChatRoomId(), userPk);
        this.chatRoom = chatRoom;
        this.isActive = true;
    }

    public void exit() {
        this.isActive = false;
    }

    public Long getUserPk() {
        return this.id.getUserPk();
    }
}