package org.example.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        System.out.println("프로듀서 메시지 " + message);
        // 메세지를 받고 처리할 부분 처리
        // 알람처리, 탈퇴후 데이터 삭제 처리, 재고 보충 처리, ....
    }
    @KafkaListener(topics = "test-topic2", groupId = "test-group")
    public void listen2(String message) {
        try {
            // 역직렬화
            OrderDto orderDto = objectMapper.readValue(message, OrderDto.class);
            System.out.println("프로듀서 메시지 " + orderDto.getMsg());
            // 메세지를 받고 처리할 부분 처리
            // 알람처리, 탈퇴후 데이터 삭제 처리, 재고 보충 처리, ....
        }catch (Exception e){}
    }
}
