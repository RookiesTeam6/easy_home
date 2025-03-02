package org.example.easyhomesurvey.Repository;

import org.example.easyhomesurvey.entity.SurveyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SurveyRepository extends JpaRepository<SurveyEntity, Integer> {
    Optional<SurveyEntity> findByTitle(String title);
}
