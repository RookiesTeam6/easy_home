package org.example.msasbuser.voteeventdto;

import lombok.Builder;
import lombok.Data;

@Data
public class OptionDeleteEventDto {
    private String eventType;
    private Integer deletedOptionPk;
    private String content;
    private Integer votePk;
    private String voteTitle;

    @Builder
    public OptionDeleteEventDto(String eventType, Integer deletedOptionPk, String content, Integer votePk, String voteTitle) {
        this.eventType = eventType;
        this.deletedOptionPk = deletedOptionPk;
        this.content = content;
        this.votePk = votePk;
        this.voteTitle = voteTitle;
    }
}
