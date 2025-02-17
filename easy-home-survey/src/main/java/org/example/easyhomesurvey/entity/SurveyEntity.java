package org.example.easyhomesurvey.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="survey")
@Data
@NoArgsConstructor
public class SurveyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer surveyPk;

    private String email;

    private String title;
    private String description;
    private LocalDateTime endDate;

    // 질문
    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SurveyQuestion> questions;

    // 질문 추가 메소드
    public void addQuestion(SurveyQuestion question) {
        questions.add(question);
        question.setSurvey(this);
    }
}
