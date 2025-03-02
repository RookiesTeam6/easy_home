package org.example.easyhomesurvey.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.easyhomesurvey.eventdto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    // 일반 문자열 메시지 전송
    public void sendMsg(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }

    // SurveyEventDto 객체를 직렬화하여 전송
    public void sendSurveyCreateEvent(String topic, SurveyEventDto surveyEventDto) throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(surveyEventDto);
        kafkaTemplate.send(topic, message);
    }

    public void sendSurveyUpdateEvent(String topic, SurveyUpdateEventDto surveyUpdateEventDto) throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(surveyUpdateEventDto);
        kafkaTemplate.send(topic, message);
    }

    public void sendQuestionCreateEvent(String topic, QuestionEventDto questionEventDto) throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(questionEventDto);
        kafkaTemplate.send(topic, message);
    }

    public void sendSurveyDeleteEvent(String topic, SurveyDeleteEventDto surveyDeleteEventDto) throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(surveyDeleteEventDto);
        kafkaTemplate.send(topic, message);
    }

    public void sendQuestionDeleteEvent(String topic, QuestionDeleteEventDto questionDeleteEventDto) throws JsonProcessingException{
        String message = objectMapper.writeValueAsString(questionDeleteEventDto);
        kafkaTemplate.send(topic, message);
    }

    public void sendQuestionUpdateEvent(String topic, QuestionUpdateEventDto questionUpdateEventDto) throws JsonProcessingException{
        String message = objectMapper.writeValueAsString(questionUpdateEventDto);
        kafkaTemplate.send(topic, message);
    }

    public void sendJoinEvent(String topic, JoinEventDto joinEventDto) throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(joinEventDto);
        kafkaTemplate.send(topic, message);
    }

}