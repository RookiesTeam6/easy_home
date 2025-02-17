package com.example.chatservice.member.dto;

import com.example.chatservice.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponse {
    private Long userPk;
    private String nickname;

    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
                .userPk(member.getUserPk())
                .nickname(member.getNickname())
                .build();
    }
}