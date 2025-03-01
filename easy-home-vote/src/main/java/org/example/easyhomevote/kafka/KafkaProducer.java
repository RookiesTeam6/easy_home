package org.example.easyhomevote.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.easyhomevote.eventdto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    // 투표 생성
    public void sendVoteCreateEvent(String topic, VoteEventDto voteEventDto) throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(voteEventDto);
        kafkaTemplate.send(topic, message);
    }

    // 투표 수정
    public void sendVoteUpdateEvent(String topic, VoteUpdateEventDto voteUpdateEventDto) throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(voteUpdateEventDto);
        kafkaTemplate.send(topic, message);
    }

    // 선택지 생성
    public void sendOptionCreateEvent(String topic, OptionEventDto optionEventDto) throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(optionEventDto);
        kafkaTemplate.send(topic, message);
    }

    // 투표 삭제
    public void sendVoteDeleteEvent(String topic, VoteDeleteEventDto voteDeleteEventDto) throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(voteDeleteEventDto);
        kafkaTemplate.send(topic, message);
    }

    // 선택지 삭제
    public void sendOptionDeleteEvent(String topic, OptionDeleteEventDto optionDeleteEventDto) throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(optionDeleteEventDto);
        kafkaTemplate.send(topic, message);
    }

    // 선택지 수정
    public void sendOptionUpdateEvent(String topic, OptionUpdateEventDto optionUpdateEventDto) throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(optionUpdateEventDto);
        kafkaTemplate.send(topic, message);
    }

    // 투표 참여
    public void sendJoinEvent(String topic, JoinEventDto joinEventDto) throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(joinEventDto);
        kafkaTemplate.send(topic, message);
    }
}
