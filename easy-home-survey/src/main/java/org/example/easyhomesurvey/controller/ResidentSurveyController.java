package org.example.easyhomesurvey.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.easyhomesurvey.Repository.SurveyQuestionRepository;
import org.example.easyhomesurvey.dto.*;
import org.example.easyhomesurvey.entity.SurveyEntity;
import org.example.easyhomesurvey.eventdto.JoinEventDto;
import org.example.easyhomesurvey.kafka.KafkaProducer;
import org.example.easyhomesurvey.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/resident/survey")
@PreAuthorize("hasRole('USER')")        // 입주민만 접근 가능
public class ResidentSurveyController {
    @Autowired
    private SurveyService surveyService;
    @Autowired
    private SurveyQuestionRepository surveyQuestionRepository;
    @Autowired
    private KafkaProducer kafkaProducer;

    // 설문 참여 - 입주민
    @PostMapping("/join")
    public ResponseEntity<String> joinSurvey(@RequestHeader("X-Auth-User") String email,
                                             @RequestBody List<AnswerDto> answerDtos) {
        surveyService.joinSurvey(email, answerDtos);

        // questionPk 리스트 추출
        List<Integer> questionPks = answerDtos.stream()
                .map(AnswerDto::getQuestionPk)
                .collect(Collectors.toList());

        // Optional로 SurveyEntity 조회
        Optional<SurveyEntity> surveyOptional = surveyQuestionRepository.findSurveyByQuestionPk(questionPks);

        // SurveyEntity가 없으면 에러 반환
        if (surveyOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("설문조사를 찾을 수 없습니다.");
        }

        SurveyEntity survey = surveyOptional.get();

        JoinEventDto joinEventDto = JoinEventDto.builder()
                .surveyPk(survey.getSurveyPk())
                .surveyTitle(survey.getTitle())
                .build();

        try {
            // 카프카로 설문 생성 이벤트 전송
            kafkaProducer.sendJoinEvent("resident-survey-events", joinEventDto);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(500).body("설문 이벤트 전송 실패");
        }

        return ResponseEntity.ok("설문조사 참여 완료");
    }

    // 설문 결과 조회
    @GetMapping("/{surveyPk}")
    public SurveyResultDto getSurveyResults(@PathVariable Integer surveyPk) {
        return surveyService.getSurveyResults(surveyPk);
    }
}
