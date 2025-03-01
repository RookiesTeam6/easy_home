package org.example.msasbuser.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.msasbuser.surveyeventdto.SurveyEventDto;
import org.example.msasbuser.voteeventdto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaVoteConsumer {
    @Autowired
    private ObjectMapper objectMapper;

    // 투표 추가
    @KafkaListener(topics = "admin-vote-events", groupId = "test-group")
    public void listenVoteCreateResponse(String message) {
        try {
            // JSON -> SurveyEventDto로 역직렬화
            VoteEventDto voteEventDto = objectMapper.readValue(message, VoteEventDto.class);

            System.out.println("Received vote event: " + voteEventDto.toString());

            // 설문 응답 처리 로직 (예: 응답 저장, 사용자 알림 등)
            System.out.println("Processing user vote response...");

        } catch (Exception e) {
            System.err.println("Failed to process vote response message: " + message);
        }
    }

    // 투표 수정
    @KafkaListener(topics = "admin-vote-events", groupId = "test-group")
    public void listenVoteUpdateResponse(String message) {
        try {
            // JSON -> SurveyEventDto로 역직렬화
            VoteUpdateEventDto voteUpdateEventDto = objectMapper.readValue(message, VoteUpdateEventDto.class);

            System.out.println("Received vote event: " + voteUpdateEventDto.toString());

            // 설문 응답 처리 로직 (예: 응답 저장, 사용자 알림 등)
            System.out.println("Processing user vote response...");

        } catch (Exception e) {
            System.err.println("Failed to process vote response message: " + message);
        }
    }

    // 투표 삭제
    @KafkaListener(topics = "admin-vote-events", groupId = "test-group")
    public void listenVoteDeleteResponse(String message) {
        try {
            // JSON -> SurveyEventDto로 역직렬화
            VoteDeleteEventDto voteDeleteEventDto = objectMapper.readValue(message, VoteDeleteEventDto.class);

            System.out.println("Received vote event: " + voteDeleteEventDto.toString());

            // 설문 응답 처리 로직 (예: 응답 저장, 사용자 알림 등)
            System.out.println("Processing user vote response...");

        } catch (Exception e) {
            System.err.println("Failed to process vote response message: " + message);
        }
    }

    // 선택지 추가
    @KafkaListener(topics = "admin-vote-events", groupId = "test-group")
    public void listenOptionCreateResponse(String message) {
        try {
            // JSON -> SurveyEventDto로 역직렬화
            OptionEventDto optionEventDto = objectMapper.readValue(message, OptionEventDto.class);

            System.out.println("Received vote event: " + optionEventDto.toString());

            // 설문 응답 처리 로직 (예: 응답 저장, 사용자 알림 등)
            System.out.println("Processing user vote response...");

        } catch (Exception e) {
            System.err.println("Failed to process vote response message: " + message);
        }
    }

    // 선택지 수정
    @KafkaListener(topics = "admin-vote-events", groupId = "test-group")
    public void listenOptionUpdateResponse(String message) {
        try {
            // JSON -> SurveyEventDto로 역직렬화
            OptionUpdateEventDto optionUpdateEventDto = objectMapper.readValue(message, OptionUpdateEventDto.class);

            System.out.println("Received vote event: " + optionUpdateEventDto.toString());

            // 설문 응답 처리 로직 (예: 응답 저장, 사용자 알림 등)
            System.out.println("Processing user vote response...");

        } catch (Exception e) {
            System.err.println("Failed to process vote response message: " + message);
        }
    }

    // 선택지 삭제
    @KafkaListener(topics = "admin-vote-events", groupId = "test-group")
    public void listenOptionDeleteResponse(String message) {
        try {
            // JSON -> SurveyEventDto로 역직렬화
            OptionDeleteEventDto optionDeleteEventDto = objectMapper.readValue(message, OptionDeleteEventDto.class);

            System.out.println("Received vote event: " + optionDeleteEventDto.toString());

            // 설문 응답 처리 로직 (예: 응답 저장, 사용자 알림 등)
            System.out.println("Processing user vote response...");

        } catch (Exception e) {
            System.err.println("Failed to process vote response message: " + message);
        }
    }

    // 투표 참여
    @KafkaListener(topics = "resident-vote-events", groupId = "test-group")
    public void listenJoinResponse(String message) {
        try {
            // JSON -> SurveyEventDto로 역직렬화
            JoinEventDto joinEventDto = objectMapper.readValue(message, JoinEventDto.class);

            System.out.println("Received vote event: " + joinEventDto.toString());

            // 설문 응답 처리 로직 (예: 응답 저장, 사용자 알림 등)
            System.out.println("Processing user vote response...");

        } catch (Exception e) {
            System.err.println("Failed to process vote response message: " + message);
        }
    }
}
