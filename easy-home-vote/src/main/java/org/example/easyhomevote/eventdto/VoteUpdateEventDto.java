package org.example.easyhomevote.eventdto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VoteUpdateEventDto {
    private String eventType;
    private Integer updatedVotePk;
    private String title;
    private String description;
    private LocalDateTime endDate;

    @Builder
    public VoteUpdateEventDto(String eventType, Integer updatedVotePk, String title, String description, LocalDateTime endDate) {
        this.eventType = eventType;
        this.updatedVotePk = updatedVotePk;
        this.title = title;
        this.description = description;
        this.endDate = endDate;
    }
}
