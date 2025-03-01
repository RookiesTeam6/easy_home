package org.example.easyhomevote.eventdto;

import lombok.Builder;
import lombok.Data;

@Data
public class VoteDeleteEventDto {
    private String eventType;
    private Integer deletedVotePk;
    private String title;

    @Builder
    public VoteDeleteEventDto(String eventType, Integer deletedVotePk, String title) {
        this.eventType = eventType;
        this.deletedVotePk = deletedVotePk;
        this.title = title;
    }
}
