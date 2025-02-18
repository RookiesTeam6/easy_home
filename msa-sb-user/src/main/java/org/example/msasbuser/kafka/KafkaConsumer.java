package org.example.msasbuser.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.msasbuser.dto.SignUpEventDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * 구독자, 이벤트를 받아서 후속 조치를 수행할 담당(혹은 타 서비스)
 */
@Service
public class KafkaConsumer {
    // 문자열 직렬, 역직렬 처리용도
    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "test-topic", groupId = "test-group")
    public void listen(String message) {
        System.out.println("프로듀서 메세지 " + message);
        // 메세지를 받고 처리할 부분 처리
        // 알람처리, 탈퇴후 데이터 삭제 처리, ....
    }

    // 회원가입 이벤트를 보낼 Kafka 토픽 이름
    @KafkaListener(topics = "user-signup-topic", groupId = "test-group")
    public void listenSignUpEvent(String message) {
        try {
            // JSON 문자열을 DTO 객체로 변환
            SignUpEventDto eventDto = objectMapper.readValue(message, SignUpEventDto.class);
            System.out.println("회원가입 이벤트 수신 : " + eventDto.toString());
            System.out.println("회원가입 된 이메일 : " + eventDto.getEmail());

            // 예: 회원가입 완료 후 이메일 전송
            // System.out.println("환영 이메일 전송: " + eventDto.getEmail());

        } catch (Exception e) {} // 메시지 처리 중 오류 발생 시 출력
    }
}
