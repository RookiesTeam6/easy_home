package org.example.msasbuser.surveyeventdto;

import lombok.Builder;

public class QuestionEventDto {
    private String eventType; // CREATE, UPDATE, DELETE
    private Integer surveyPk;
    private String surveyTitle;
    private String question;

    @Builder
    public QuestionEventDto(String eventType, Integer surveyPk, String surveyTitle, String question) {
        this.eventType = eventType;
        this.surveyPk = surveyPk;
        this.surveyTitle = surveyTitle;
        this.question = question;
    }
}
