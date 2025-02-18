package org.example.msasbuser.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.msasbuser.dto.SignUpEventDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate; // Kafka 메시지를 전송하는 템플릿

    @Autowired
    private ObjectMapper objectMapper; // 객체를 JSON 문자열로 변환하는 Jackson 라이브러리

    // 재료는 OrderDto객체 -> 문자열직열화 처리 -> 메세지 형태는 문자열
    // 재료는 OrderDto객체 <- 문자열역직열화 처리 <- 메세지 형태는 문자열
    public void sendMsg(String topic, SignUpEventDto SignUpEventDto) throws JsonProcessingException {
        kafkaTemplate.send(topic, objectMapper.writeValueAsString(SignUpEventDto));
    }
}
