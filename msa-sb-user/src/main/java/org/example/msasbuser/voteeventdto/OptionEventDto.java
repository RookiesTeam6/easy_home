package org.example.msasbuser.voteeventdto;

import lombok.Builder;
import lombok.Data;

@Data
public class OptionEventDto {
    private String eventType;
    private Integer votePk;
    private String voteTitle;
    private String newOption;

    @Builder
    public OptionEventDto(String eventType, Integer votePk, String voteTitle, String newOption) {
        this.eventType = eventType;
        this.votePk = votePk;
        this.voteTitle = voteTitle;
        this.newOption = newOption;
    }
}
