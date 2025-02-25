package org.example.easyhomesurvey.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class QuestionResultDto {
    private Integer questionPk;
    private String question;
    private List<String> answers;
}
