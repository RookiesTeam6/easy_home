package org.example.msasbuser.surveyeventdto;

import lombok.Builder;
import lombok.Data;

@Data
public class QuestionUpdateEventDto {
    private String eventType;
    private Integer questionPk;
    private String question;
    private Integer surveyPk;
    private String surveyTitle;

    @Builder
    public QuestionUpdateEventDto(String eventType, Integer questionPk, String question, Integer surveyPk, String surveyTitle) {
        this.eventType = eventType;
        this.questionPk = questionPk;
        this.question = question;
        this.surveyPk = surveyPk;
        this.surveyTitle = surveyTitle;
    }
}
