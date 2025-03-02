package org.example.easyhomevote.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
public class VoteUpdateReqDto {
    private String title;
    private String description;
    private LocalDateTime endDate;
}
