package org.example.easyhomesurvey.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityNotFoundException;
import org.example.easyhomesurvey.Repository.SurveyQuestionRepository;
import org.example.easyhomesurvey.Repository.SurveyRepository;
import org.example.easyhomesurvey.dto.*;
import org.example.easyhomesurvey.entity.SurveyEntity;
import org.example.easyhomesurvey.entity.SurveyQuestion;
import org.example.easyhomesurvey.eventdto.*;
import org.example.easyhomesurvey.kafka.KafkaProducer;
import org.example.easyhomesurvey.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/admin/survey")
@PreAuthorize("hasRole('ADMIN')")       // 관리자만 접근 가능
public class AdminSurveyController {
    @Autowired
    private SurveyService surveyService;

    @Autowired
    private KafkaProducer kafkaProducer;
    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private SurveyQuestionRepository surveyQuestionRepository;

    // 설문 등록 - 관리자
    @PostMapping("")
    public ResponseEntity<String> createSurvey(@RequestHeader("X-Auth-User") String email,
                                               @RequestBody SurveyDto surveyDto) {
        surveyService.createSurvey(email, surveyDto);

        // 설문 생성 이벤트 카프카로 전송
        SurveyEventDto surveyEventDto = SurveyEventDto.builder()
                .eventType("CREATE") // 이벤트 타입
                .surveyTitle(surveyDto.getTitle())
                .surveyDescription(surveyDto.getDescription())
                .questions(surveyDto.getQuestions())
                .build();

        try {
            // 카프카로 설문 생성 이벤트 전송
            kafkaProducer.sendSurveyCreateEvent("admin-survey-events", surveyEventDto);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(500).body("설문 이벤트 전송 실패");
        }

        return ResponseEntity.ok("설문조사 생성 완료");
    }

    // 설문 결과 조회
    @GetMapping("/{surveyPk}")
    public SurveyResultDto getSurveyResults(@PathVariable Integer surveyPk) {
        return surveyService.getSurveyResults(surveyPk);
    }

    // 설문 목록 조회
    @GetMapping("/list")
    public ResponseEntity<List<SurveyListDto>> getSurveyList() {
        List<SurveyListDto> surveys = surveyService.getAllSurveys();
        return ResponseEntity.ok(surveys);
    }

    // 설문 수정
    @PutMapping("/{surveyPk}")
    public ResponseEntity<SurveyUpdateResDto> updateSurvey(
            @PathVariable Integer surveyPk,
            @RequestBody SurveyUpdateReqDto requestDto) {
        SurveyUpdateResDto updatedSurvey = surveyService.updateSurvey(surveyPk, requestDto);

        // 설문 생성 이벤트 카프카로 전송
        SurveyUpdateEventDto surveyUpdateEventDto = SurveyUpdateEventDto.builder()
                .eventType("UPDATE") // 이벤트 타입
                .surveyPk(updatedSurvey.getSurveyPk())
                .title(updatedSurvey.getTitle())
                .description(updatedSurvey.getDescription())
                .endDate(updatedSurvey.getEndDate())
                .build();

        try {
            // 카프카로 설문 생성 이벤트 전송
            kafkaProducer.sendSurveyUpdateEvent("admin-survey-events", surveyUpdateEventDto);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "설문 이벤트 전송 실패", e);
        }

        return ResponseEntity.ok(updatedSurvey);
    }

    // 질문 추가
    @PostMapping("/{surveyPk}/question")
    public ResponseEntity<String> addQuestion(@PathVariable Integer surveyPk,
                                                      @RequestBody QuestionDto questionDto) {
        SurveyQuestion newQuestion = surveyService.addQuestion(surveyPk, questionDto);

        SurveyEntity survey = surveyRepository.findById(surveyPk)
                .orElseThrow(() -> new EntityNotFoundException("Survey not found with id: " + surveyPk));

        // 설문 생성 이벤트 카프카로 전송
        QuestionEventDto questionEventDto = QuestionEventDto.builder()
                .eventType("UPDATE") // 이벤트 타입
                .surveyPk(surveyPk)
                .surveyTitle(survey.getTitle())
                .question(newQuestion.getQuestion())
                .build();

        try {
            // 카프카로 설문 생성 이벤트 전송
            kafkaProducer.sendQuestionCreateEvent("admin-survey-events", questionEventDto);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(500).body("설문 이벤트 전송 실패");
        }

        return ResponseEntity.ok("질문 추가 완료");
    }

    // 질문 수정
    @PutMapping("/{surveyPk}/question/{questionPk}")
    public ResponseEntity<QuestionUpdateResDto> updateQuestion(@PathVariable Integer surveyPk,
                                                         @PathVariable Integer questionPk,
                                                         @RequestBody QuestionDto questionDto) {
        QuestionUpdateResDto updatedQuestionDto = surveyService.updateQuestion(surveyPk, questionPk, questionDto);

        SurveyEntity updatedSurvey = surveyRepository.findById(surveyPk)
                .orElseThrow(() -> new IllegalArgumentException("Survey not found"));
        SurveyQuestion updatedQuestion = surveyQuestionRepository.findById(questionPk)
                .orElseThrow(()-> new IllegalArgumentException("Question not found"));
        // 설문 삭제 이벤트 카프카로 전송
        QuestionUpdateEventDto questionUpdateEventDto = QuestionUpdateEventDto.builder()
                .eventType("UPDATE") // 이벤트 타입
                .questionPk(questionPk)
                .question(updatedQuestion.getQuestion())
                .surveyPk(surveyPk)
                .surveyTitle(updatedSurvey.getTitle())
                .build();

        try {
            // 카프카로 설문 삭제 이벤트 전송
            kafkaProducer.sendQuestionUpdateEvent("admin-survey-events", questionUpdateEventDto);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "설문 이벤트 전송 실패", e);
        }

        return ResponseEntity.ok(updatedQuestionDto);
    }

    // 질문 삭제
    @DeleteMapping("/{surveyPk}/question/{questionPk}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Integer surveyPk,
                                                 @PathVariable Integer questionPk) {

        SurveyEntity survey = surveyRepository.findById(surveyPk)
                .orElseThrow(() -> new IllegalArgumentException("Survey not found"));
        SurveyQuestion deletedQuestion = surveyQuestionRepository.findById(questionPk)
                .orElseThrow(() -> new IllegalArgumentException("question not found"));
        // 설문 삭제 이벤트 카프카로 전송
        QuestionDeleteEventDto questionDeleteEventDto = QuestionDeleteEventDto.builder()
                .eventType("UPDATE") // 이벤트 타입
                .questionPk(questionPk)
                .question(deletedQuestion.getQuestion())
                .surveyPk(surveyPk)
                .surveyTitle(survey.getTitle())
                .build();

        surveyService.deleteQuestion(surveyPk, questionPk);

        try {
            // 카프카로 설문 삭제 이벤트 전송
            kafkaProducer.sendQuestionDeleteEvent("admin-survey-events", questionDeleteEventDto);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "설문 이벤트 전송 실패", e);
        }

        return ResponseEntity.ok("질문 삭제 완료");
    }

    // 설문 삭제
    @DeleteMapping("/{surveyPk}")
    public ResponseEntity<String> deleteSurvey(@PathVariable Integer surveyPk) {
        SurveyEntity deletedsurvey = surveyRepository.findById(surveyPk)
            .orElseThrow(() -> new IllegalArgumentException("Survey not found"));
        // 설문 삭제 이벤트 카프카로 전송
        SurveyDeleteEventDto surveyDeleteEventDto = SurveyDeleteEventDto.builder()
                .eventType("DELETE") // 이벤트 타입
                .surveyPk(deletedsurvey.getSurveyPk())
                .title(deletedsurvey.getTitle())
                .build();

        surveyService.deleteSurvey(surveyPk);

        try {
            // 카프카로 설문 삭제 이벤트 전송
            kafkaProducer.sendSurveyDeleteEvent("admin-survey-events", surveyDeleteEventDto);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "설문 이벤트 전송 실패", e);
        }

        return ResponseEntity.ok("설문조사 삭제 완료");
    }
}
