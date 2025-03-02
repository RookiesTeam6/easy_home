package com.example.chatservice.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor  // Jackson에서 역직렬화를 위해 필요
@AllArgsConstructor // Builder 패턴 사용을 위해 필요
public class MemberEvent {
    private String eventType;  // "CREATE", "UPDATE", "DELETE"
    private String username;   // Member의 nickname에 매핑될 필드
}
