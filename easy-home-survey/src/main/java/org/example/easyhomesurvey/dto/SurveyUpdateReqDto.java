package org.example.easyhomesurvey.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
public class SurveyUpdateReqDto {
    private String title;
    private String description;
    private LocalDateTime endDate;
}
