package com.example.chatservice.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SimpleMemberResponse {
    private Long userPk;
    private String nickname;
}