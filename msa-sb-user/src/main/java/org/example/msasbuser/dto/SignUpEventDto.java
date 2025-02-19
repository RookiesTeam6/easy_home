package org.example.msasbuser.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class SignUpEventDto {
    private String email;
    private String nickname;

    @Builder
    public SignUpEventDto(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }
}
