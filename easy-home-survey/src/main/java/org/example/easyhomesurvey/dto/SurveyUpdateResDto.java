package org.example.easyhomesurvey.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.easyhomesurvey.entity.SurveyEntity;

import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
public class SurveyUpdateResDto {
    private Integer surveyPk;
    private String title;
    private String description;
    private LocalDateTime endDate;

    // 엔티티를 dto로 변환
    public SurveyUpdateResDto(SurveyEntity survey) {
        this.surveyPk = survey.getSurveyPk();
        this.title = survey.getTitle();
        this.description = survey.getDescription();
        this.endDate = survey.getEndDate();
    }
}
