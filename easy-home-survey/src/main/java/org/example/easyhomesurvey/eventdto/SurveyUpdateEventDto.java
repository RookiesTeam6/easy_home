package org.example.easyhomesurvey.eventdto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SurveyUpdateEventDto {
    private String eventType; // CREATE, UPDATE, DELETE
    private Integer surveyPk;
    private String title;
    private String description;
    private LocalDateTime endDate;

    @Builder
    public SurveyUpdateEventDto(String eventType, Integer surveyPk, String title, String description, LocalDateTime endDate) {
        this.eventType = eventType;
        this.surveyPk = surveyPk;
        this.title = title;
        this.description = description;
        this.endDate = endDate;
    }
}
