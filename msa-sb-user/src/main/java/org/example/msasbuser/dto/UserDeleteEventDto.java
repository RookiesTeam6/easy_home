package org.example.msasbuser.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class UserDeleteEventDto {
    private String email; // 탈퇴한 사용자 이메일
    private String nickname; // 탈퇴한 사용자 닉네임

    @Builder
    public UserDeleteEventDto(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }
}
