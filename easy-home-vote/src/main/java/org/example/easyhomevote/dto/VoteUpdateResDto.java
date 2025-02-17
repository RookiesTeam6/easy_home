package org.example.easyhomevote.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.easyhomevote.entity.VoteEntity;

import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
public class VoteUpdateResDto {
    private Integer votePk;
    private String title;
    private String description;
    private LocalDateTime endDate;

    // 엔티티를 dto로 변환
    public VoteUpdateResDto(VoteEntity survey) {
        this.votePk = survey.getVotePk();
        this.title = survey.getTitle();
        this.description = survey.getDescription();
        this.endDate = survey.getEndDate();
    }
}
