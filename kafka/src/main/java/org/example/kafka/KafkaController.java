package org.example.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 트리거 용도
 */
@RestController
@RequestMapping("kafka")
public class KafkaController {
    @Autowired
    private KafkaProducer producer;

    @GetMapping("/send")
    public String sendMessage(@RequestParam("message") String message) {
        // 요청(트리거) -> 이벤트 발생(프로듀서)
        // (토픽, 메세지)
        producer.sendMsg("test-topic", message);
        return "메세지 전송 완료  : " + message;
    }
    @PostMapping("/send")
    public String sendMessage(@RequestBody OrderDto orderDto) {
        // 요청(트리거) -> 이벤트 발생(프로듀서)
        // (토픽, 메세지)
        try{
            producer.sendMsg("test-topic2", orderDto);
        }catch (Exception e){}
        return "메세지 전송 완료  : " + orderDto.getMsg();
    }
}
