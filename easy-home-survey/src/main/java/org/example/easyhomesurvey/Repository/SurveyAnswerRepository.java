package org.example.easyhomesurvey.Repository;

import org.example.easyhomesurvey.entity.SurveyAnswer;
import org.example.easyhomesurvey.entity.SurveyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyAnswerRepository extends JpaRepository<SurveyAnswer, Integer> {
    List<SurveyAnswer> findByQuestion(SurveyQuestion question);
}
