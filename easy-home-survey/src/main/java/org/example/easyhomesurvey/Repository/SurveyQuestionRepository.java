package org.example.easyhomesurvey.Repository;

import org.example.easyhomesurvey.entity.SurveyEntity;
import org.example.easyhomesurvey.entity.SurveyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion, Integer> {
    List<SurveyQuestion> findBySurvey(SurveyEntity survey);
}
