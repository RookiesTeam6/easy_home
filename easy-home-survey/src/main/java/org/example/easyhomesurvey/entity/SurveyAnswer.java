package org.example.easyhomesurvey.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="survey_answer")
@NoArgsConstructor
public class SurveyAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer answerPk;

    @ManyToOne
    @JoinColumn(name="question_pk", nullable=false)
    private SurveyQuestion question;

    private String email;

    private String answer;
}
