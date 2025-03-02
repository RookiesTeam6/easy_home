package org.example.easyhomesurvey.eventdto;

import lombok.Builder;
import lombok.Data;
import org.example.easyhomesurvey.dto.QuestionDto;

import java.util.List;

@Data
public class SurveyEventDto {
    private String eventType; // CREATE, UPDATE, DELETE
    private String surveyTitle;
    private String surveyDescription;
    private List<QuestionDto> questions;

    @Builder
    public SurveyEventDto(String eventType, String surveyTitle, String surveyDescription, List<QuestionDto> questions) {
        this.eventType = eventType;
        this.surveyTitle = surveyTitle;
        this.surveyDescription = surveyDescription;
        this.questions = questions;
    }
}
