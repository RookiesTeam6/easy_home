package org.example.msasbuser.surveyeventdto;

import lombok.Builder;
import lombok.Data;

@Data
public class JoinEventDto {
    private Integer surveyPk;
    private String surveyTitle;

    @Builder
    public JoinEventDto(Integer surveyPk, String surveyTitle) {
        this.surveyPk = surveyPk;
        this.surveyTitle = surveyTitle;
    }
}
