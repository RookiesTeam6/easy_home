package org.example.msasbuser.surveyeventdto;

import lombok.Builder;
import lombok.Data;

@Data
public class SurveyDeleteEventDto {
    private String eventType;
    private Integer surveyPk;
    private String title;

    @Builder
    public SurveyDeleteEventDto(String eventType, Integer surveyPk, String title) {
        this.eventType = eventType;
        this.surveyPk = surveyPk;
        this.title = title;
    }
}
