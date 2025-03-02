package com.example.chatservice.message.service;

import com.example.chatservice.chatroom.entity.ChatRoom;
import com.example.chatservice.chatroom.repository.ChatRoomRepository;
import com.example.chatservice.message.dto.request.MessageRequest;
import com.example.chatservice.message.dto.response.MessageResponse;
import com.example.chatservice.message.entity.ChatMessage;
import com.example.chatservice.message.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageService {
    private final ChatMessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;  // 이 부분 추가

    @Transactional
    public MessageResponse sendMessage(MessageRequest request) {
        Long roomId = request.getChatRoomId();

        // roomName으로 조회 시 디버깅 로그 추가
        if (roomId == null && request.getRoomName() != null) {
            try {
                System.out.println("Searching for room with name: " + request.getRoomName());
                Optional<ChatRoom> chatRoomOpt = chatRoomRepository.findByRoomName(request.getRoomName());

                if (chatRoomOpt.isPresent()) {
                    ChatRoom chatRoom = chatRoomOpt.get();
                    roomId = chatRoom.getChatRoomId();
                    System.out.println("Found room with ID: " + roomId);
                } else {
                    System.out.println("No room found with name: " + request.getRoomName());
                    throw new RuntimeException("ChatRoom not found with name: " + request.getRoomName());
                }
            } catch (Exception e) {
                System.err.println("Error finding room by name: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Error processing roomName: " + e.getMessage(), e);
            }
        }

        if (roomId == null) {
            throw new IllegalArgumentException("Either chatRoomId or roomName must be provided");
        }

        ChatMessage message = ChatMessage.builder()
                .chatRoomId(roomId)
                .senderPk(request.getSenderPk())
                .content(request.getContent())
                .build();

        return MessageResponse.from(messageRepository.save(message));
    }

    public Page<MessageResponse> getRoomMessages(Long roomId, Pageable pageable) {
        return messageRepository.findByChatRoomIdOrderBySentAtDesc(roomId, pageable)
                .map(MessageResponse::from);
    }

    @Transactional
    public void markAsRead(Long messageId) {
        ChatMessage message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        message.markAsRead();
    }

    // 채팅방 이름으로 메시지 조회하는 메서드 추가
    public Page<MessageResponse> getRoomMessagesByName(String roomName, Pageable pageable) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomName(roomName)
                .orElseThrow(() -> new RuntimeException("ChatRoom not found with name: " + roomName));

        return messageRepository.findByChatRoomIdOrderBySentAtDesc(chatRoom.getChatRoomId(), pageable)
                .map(MessageResponse::from);
    }
}