package org.example.easyhomesurvey.controller;

import org.example.easyhomesurvey.dto.*;
import org.example.easyhomesurvey.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resident/survey")
@PreAuthorize("hasRole('USER')")        // 입주민만 접근 가능
public class ResidentSurveyController {
    @Autowired
    private SurveyService surveyService;

    // 설문 참여 - 입주민
    @PostMapping("/join")
    public ResponseEntity<String> joinSurvey(@RequestHeader("X-Auth-User") String email,
                                             @RequestBody List<AnswerDto> answerDtos) {
        surveyService.joinSurvey(email, answerDtos);
        return ResponseEntity.ok("설문조사 참여 완료");
    }

    // 설문 결과 조회
    @GetMapping("/{surveyPk}")
    public SurveyResultDto getSurveyResults(@PathVariable Integer surveyPk) {
        return surveyService.getSurveyResults(surveyPk);
    }
}
