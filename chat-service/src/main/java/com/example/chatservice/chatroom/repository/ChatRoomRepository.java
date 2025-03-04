package com.example.chatservice.chatroom.repository;

import com.example.chatservice.chatroom.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByRoomName(String roomName);

}