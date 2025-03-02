package com.example.chatservice.chatroom.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatRoomMemberId implements Serializable {
    private Long chatRoomId;
    private Long userPk;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatRoomMemberId that = (ChatRoomMemberId) o;
        return Objects.equals(chatRoomId, that.chatRoomId) &&
                Objects.equals(userPk, that.userPk);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatRoomId, userPk);
    }
}