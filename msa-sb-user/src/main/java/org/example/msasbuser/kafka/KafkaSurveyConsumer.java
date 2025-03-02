package org.example.msasbuser.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.msasbuser.surveyeventdto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * 구독자, 이벤트를 받아서 후속 조치를 수행할 담당(혹은 타 서비스)
 */
@Service
public class KafkaSurveyConsumer {
    // 문자열 직렬, 역직렬 처리용도
    @Autowired
    private ObjectMapper objectMapper;

    // 설문 추가
    @KafkaListener(topics = "admin-survey-events", groupId = "test-group")
    public void listenSurveyCreateResponse(String message) {
        try {
            // JSON -> SurveyEventDto로 역직렬화
            SurveyEventDto surveyEventDto = objectMapper.readValue(message, SurveyEventDto.class);

            System.out.println("Received survey event: " + surveyEventDto.toString());

            // 설문 응답 처리 로직 (예: 응답 저장, 사용자 알림 등)
            System.out.println("Processing user survey response...");

        } catch (Exception e) {
            System.err.println("Failed to process survey response message: " + message);
        }
    }

    // 설문 수정
    @KafkaListener(topics = "admin-survey-events", groupId = "test-group")
    public void listenSurveyUpdateResponse(String message) {
        try {
            SurveyUpdateEventDto surveyUpdateEventDto = objectMapper.readValue(message, SurveyUpdateEventDto.class);

            System.out.println("Received survey event: " + surveyUpdateEventDto.toString());

            // 설문 응답 처리 로직 (예: 응답 저장, 사용자 알림 등)
            System.out.println("Processing user survey response...");

        } catch (Exception e) {
            System.err.println("Failed to process survey response message: " + message);
        }
    }

    // 질문 추가
    @KafkaListener(topics = "admin-survey-events", groupId = "test-group")
    public void listenQuestionCreateResponse(String message) {
        try {
            // JSON -> SurveyEventDto로 역직렬화
            QuestionEventDto questionEventDto = objectMapper.readValue(message, QuestionEventDto.class);

            System.out.println("Received survey event: " + questionEventDto.toString());

            // 설문 응답 처리 로직 (예: 응답 저장, 사용자 알림 등)
            System.out.println("Processing user survey response...");

        } catch (Exception e) {
            System.err.println("Failed to process survey response message: " + message);
        }
    }

    // 설문 삭제
    @KafkaListener(topics = "admin-survey-events", groupId = "test-group")
    public void listenSurveyDeleteResponse(String message) {
        try {
            // JSON -> SurveyEventDto로 역직렬화
            SurveyDeleteEventDto surveyDeleteEventDto = objectMapper.readValue(message, SurveyDeleteEventDto.class);

            System.out.println("Received survey event: " + surveyDeleteEventDto.toString());

            // 설문 응답 처리 로직 (예: 응답 저장, 사용자 알림 등)
            System.out.println("Processing user survey response...");

        } catch (Exception e) {
            System.err.println("Failed to process survey response message: " + message);
        }
    }

    // 질문 수정
    @KafkaListener(topics = "admin-survey-events", groupId = "test-group")
    public void listenQuestionUpdateResponse(String message) {
        try {
            // JSON -> SurveyEventDto로 역직렬화
            QuestionUpdateEventDto questionUpdateEventDto = objectMapper.readValue(message, QuestionUpdateEventDto.class);

            System.out.println("Received survey event: " + questionUpdateEventDto.toString());

            // 설문 응답 처리 로직 (예: 응답 저장, 사용자 알림 등)
            System.out.println("Processing user survey response...");

        } catch (Exception e) {
            System.err.println("Failed to process survey response message: " + message);
        }
    }

    // 질문 삭제
    @KafkaListener(topics = "admin-survey-events", groupId = "test-group")
    public void listenQuestionDeleteResponse(String message) {
        try {
            // JSON -> SurveyEventDto로 역직렬화
            QuestionDeleteEventDto questionDeleteEventDto = objectMapper.readValue(message, QuestionDeleteEventDto.class);

            System.out.println("Received survey event: " + questionDeleteEventDto.toString());

            // 설문 응답 처리 로직 (예: 응답 저장, 사용자 알림 등)
            System.out.println("Processing user survey response...");

        } catch (Exception e) {
            System.err.println("Failed to process survey response message: " + message);
        }
    }

    // 설문 참여
    @KafkaListener(topics = "resident-survey-events", groupId = "test-group")
    public void listenJoinResponse(String message) {
        try {
            // JSON -> SurveyEventDto로 역직렬화
            JoinEventDto joinEventDto = objectMapper.readValue(message, JoinEventDto.class);

            System.out.println("Received survey event: " + joinEventDto.toString());

            // 설문 응답 처리 로직 (예: 응답 저장, 사용자 알림 등)
            System.out.println("Processing user survey response...");

        } catch (Exception e) {
            System.err.println("Failed to process survey response message: " + message);
        }
    }
}