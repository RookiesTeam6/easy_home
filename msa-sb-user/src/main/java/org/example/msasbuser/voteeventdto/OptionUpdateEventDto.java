package org.example.msasbuser.voteeventdto;

import lombok.Builder;
import lombok.Data;

@Data
public class OptionUpdateEventDto {
    private String eventType;
    private Integer updatedOptionPk;
    private String option;
    private Integer votePk;
    private String voteTitle;

    @Builder
    public OptionUpdateEventDto(String eventType, Integer updatedOptionPk, String option, Integer votePk, String voteTitle) {
        this.eventType = eventType;
        this.updatedOptionPk = updatedOptionPk;
        this.option = option;
        this.votePk = votePk;
        this.voteTitle = voteTitle;
    }
}
