package org.example.msasbuser.voteeventdto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class VoteEventDto {
    private String eventType; // CREATE, UPDATE, DELETE
    private String newVoteTitle;
    private String voteDescription;
    private List<OptionDto> options;

    @Builder
    public VoteEventDto(String eventType, String newVoteTitle, String voteDescription, List<OptionDto> options) {
        this.eventType = eventType;
        this.newVoteTitle = newVoteTitle;
        this.voteDescription = voteDescription;
        this.options = options;
    }

}
