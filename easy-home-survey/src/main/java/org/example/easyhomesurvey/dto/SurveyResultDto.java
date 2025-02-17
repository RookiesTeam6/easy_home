package org.example.easyhomesurvey.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class SurveyResultDto {
    private String title;
    private String description;
    private List<QuestionResultDto> questions;
}
