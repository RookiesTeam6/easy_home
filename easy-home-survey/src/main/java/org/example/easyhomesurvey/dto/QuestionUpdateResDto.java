package org.example.easyhomesurvey.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.easyhomesurvey.entity.SurveyQuestion;

@Data
@ToString
@NoArgsConstructor
public class QuestionUpdateResDto {
    private Integer questionPk;
    private String question;

    // 설문조사 정보
    private Integer surveyPk;
    private String surveyTitle;

    // 엔티티를 dto로 변환
    public QuestionUpdateResDto(SurveyQuestion question) {
        this.questionPk = question.getQuestionPk();
        this.question = question.getQuestion();
        this.surveyPk = question.getSurvey().getSurveyPk();
        this.surveyTitle = question.getSurvey().getTitle();
    }
}