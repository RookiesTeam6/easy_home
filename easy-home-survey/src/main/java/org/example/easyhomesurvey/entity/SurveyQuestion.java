package org.example.easyhomesurvey.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="survey_question")
@Data
@NoArgsConstructor
public class SurveyQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer questionPk;

    // 인증 기능 추가 시 외래키 삭제
    // private Integer surveyPk;
    @ManyToOne
    @JoinColumn(name="survey_pk", nullable=false)
    private SurveyEntity survey;

    private String question;

    // 응답
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SurveyAnswer> answers;
}
