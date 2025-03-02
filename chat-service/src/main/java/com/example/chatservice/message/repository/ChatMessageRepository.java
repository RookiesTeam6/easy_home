package com.example.chatservice.message.repository;

import com.example.chatservice.chatroom.entity.ChatRoom;
import com.example.chatservice.message.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Page<ChatMessage> findByChatRoomIdOrderBySentAtDesc(Long chatRoomId, Pageable pageable);
   // Optional<ChatRoom> findByRoomName(String roomName);
}