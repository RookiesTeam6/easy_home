package com.example.chatservice.chatroom.repository;

import com.example.chatservice.chatroom.entity.ChatRoomMember;
import com.example.chatservice.chatroom.entity.ChatRoomMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, ChatRoomMemberId> {
    @Query("SELECT cm FROM ChatRoomMember cm WHERE cm.id.userPk = :userPk AND cm.isActive = true")
    List<ChatRoomMember> findByUserPkAndIsActiveTrue(@Param("userPk") Long userPk);
}