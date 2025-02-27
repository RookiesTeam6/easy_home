package com.example.chatservice.member.config;

import com.example.chatservice.member.entity.Member;
import com.example.chatservice.member.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MemberEventConsumer {
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "user-signup-topic", groupId = "chat-service")
    public void handleSignUpEvent(String message) {
        try {
            // 문자열 메시지를 SignUpEventDto로 변환
            SignUpEventDto event = objectMapper.readValue(message, SignUpEventDto.class);
            log.info("회원가입 이벤트 수신: {}", event);

            // 닉네임으로 중복 확인 후 저장
            if (!memberRepository.existsByNickname(event.getNickname())) {
                Member savedMember = memberRepository.save(Member.builder()
                        .nickname(event.getNickname())
                        .address(event.getAddress())  // getter 메서드명 변경
                        .build());
                log.info("새 회원 등록 완료: {}, 주소: {}", savedMember.getNickname(), savedMember.getAddress());
            } else {
                log.info("이미 존재하는 닉네임: {}", event.getNickname());
            }
        } catch (Exception e) {
            log.error("회원가입 이벤트 처리 중 오류 발생", e);
        }
    }

    // 회원가입 이벤트 DTO (내부 클래스로 정의)
    private static class SignUpEventDto {
        private String email;
        private String nickname;
        private String address;  // address 필드 추가

        // Getters & Setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getNickname() { return nickname; }
        public void setNickname(String nickname) { this.nickname = nickname; }
        public String getAddress() { return address; }  // address getter 추가
        public void setAddress(String address) { this.address = address; }  // address setter 추가


        @Override
        public String toString() {
            return "SignUpEventDto{email='" + email + "', nickname='" + nickname + "', address='" + address + "'}";
        }
    }
}