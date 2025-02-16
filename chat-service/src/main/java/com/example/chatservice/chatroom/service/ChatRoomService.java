package com.example.chatservice.chatroom.service;

import com.example.chatservice.chatroom.dto.request.CreateChatRoomRequest;
import com.example.chatservice.chatroom.dto.response.ChatRoomResponse;
import com.example.chatservice.chatroom.entity.ChatRoom;
import com.example.chatservice.chatroom.entity.ChatRoomMember;
import com.example.chatservice.chatroom.entity.ChatRoomMemberId;
import com.example.chatservice.chatroom.repository.ChatRoomMemberRepository;
import com.example.chatservice.chatroom.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    @Transactional
    public ChatRoomResponse createChatRoom(CreateChatRoomRequest request) {
        ChatRoom chatRoom = new ChatRoom(request.getUserPk1(), request.getUserPk2());
        return ChatRoomResponse.from(chatRoomRepository.save(chatRoom));
    }

    public List<ChatRoomResponse> getUserChatRooms(Long userPk) {
        return chatRoomMemberRepository.findByUserPkAndIsActiveTrue(userPk).stream()
                .map(ChatRoomMember::getChatRoom)
                .map(ChatRoomResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void exitChatRoom(Long roomId, Long userPk) {
        ChatRoomMember member = chatRoomMemberRepository.findById(
                        new ChatRoomMemberId(roomId, userPk))
                .orElseThrow(() -> new RuntimeException("Chat room member not found"));
        member.exit();
    }
}