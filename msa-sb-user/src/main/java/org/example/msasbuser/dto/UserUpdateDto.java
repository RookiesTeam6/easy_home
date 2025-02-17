package org.example.msasbuser.dto;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {
    private String userName;
    private String address;
}
