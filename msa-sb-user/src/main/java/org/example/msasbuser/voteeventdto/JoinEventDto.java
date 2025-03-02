package org.example.msasbuser.voteeventdto;

import lombok.Builder;
import lombok.Data;

@Data
public class JoinEventDto {
    private Integer joinedVotePk;
    private String voteTitle;

    @Builder
    public JoinEventDto(Integer joinedVotePk, String voteTitle) {
        this.joinedVotePk = joinedVotePk;
        this.voteTitle = voteTitle;
    }
}
