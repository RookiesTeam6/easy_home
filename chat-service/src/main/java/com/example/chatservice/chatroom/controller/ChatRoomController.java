package com.example.chatservice.chatroom.controller;

import com.example.chatservice.chatroom.dto.request.CreateChatRoomRequest;
import com.example.chatservice.chatroom.dto.response.ChatRoomResponse;
import com.example.chatservice.chatroom.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resident/chat/chat-rooms")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @PostMapping
    public ResponseEntity<ChatRoomResponse> createChatRoom(@RequestBody CreateChatRoomRequest request) {
        return ResponseEntity.ok(chatRoomService.createChatRoom(request));
    }

    @GetMapping("/users/{userPk}")
    public ResponseEntity<List<ChatRoomResponse>> getUserChatRooms(@PathVariable Long userPk) {
        return ResponseEntity.ok(chatRoomService.getUserChatRooms(userPk));
    }

    @PutMapping("/{roomId}/exit")
    public ResponseEntity<Void> exitChatRoom(
            @PathVariable Long roomId,
            @RequestParam Long userPk) {
        chatRoomService.exitChatRoom(roomId, userPk);
        return ResponseEntity.ok().build();
    }
}