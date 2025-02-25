package org.example.easyhomesurvey.Repository;

import org.example.easyhomesurvey.entity.SurveyEntity;
import org.example.easyhomesurvey.entity.SurveyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion, Integer> {
    List<SurveyQuestion> findBySurvey(SurveyEntity survey);

    Integer question(String question);

    @Query("SELECT q.survey FROM SurveyQuestion q WHERE q.questionPk IN :questionPks")
    Optional<SurveyEntity> findSurveyByQuestionPk(@Param("questionPks") List<Integer> questionPks);
}
