package org.example.easyhomesurvey.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.easyhomesurvey.Repository.*;
import org.example.easyhomesurvey.dto.*;
import org.example.easyhomesurvey.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SurveyService {
    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private SurveyQuestionRepository surveyQuestionRepository;
    @Autowired
    private SurveyAnswerRepository surveyAnswerRepository;

    // 설문 등록 - 관리자
    public void createSurvey(String email, SurveyDto surveyDto) {
        /*// 관리자번호가 없거나 존재하지 않으면 안됨 (인증 후 수정)
        if (surveyDto.getManagerPk() == null) {
            throw new IllegalArgumentException("ManagerPk is null");
        }
        ManagerEntity manager = managerRepository.findById(surveyDto.getManagerPk())
                .orElseThrow(() -> new IllegalArgumentException("Manager does not exist"));
*/
        // title이 비면 안됨
        if (surveyDto.getTitle() == null || surveyDto.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        // title 중복 여부 확인
        if (surveyRepository.findByTitle(surveyDto.getTitle()).isPresent()) {
            throw new IllegalArgumentException("Title already exists");
        }

        // 설문  생성
        SurveyEntity surveyEntity = new SurveyEntity();
        surveyEntity.setEmail(email);
        surveyEntity.setTitle(surveyDto.getTitle());
        surveyEntity.setDescription(surveyDto.getDescription());
        surveyEntity.setEndDate(surveyDto.getEndDate());
        surveyRepository.save(surveyEntity);

        // 질문 리스트 저장
        List<QuestionDto> questions = surveyDto.getQuestions();
            // 최소 1개의 질문이 있어야함
        // 추가 - question 중복 여부 확인
        if (questions == null || questions.isEmpty()) {
            throw new IllegalArgumentException("At least one question must be provided");
        }
        for (QuestionDto questionDto : questions) {
            SurveyQuestion questionEntity = new SurveyQuestion();
            questionEntity.setQuestion(questionDto.getQuestion());
            questionEntity.setSurvey(surveyEntity);
            surveyQuestionRepository.save(questionEntity);
        }
    }
    // 설문 참여 - 입주민
    public void joinSurvey(String email, List<AnswerDto> answerDtos) {
        for (AnswerDto answerDto : answerDtos) {
           /* // 회원번호 조회
            if (answerDto.getUserPk() == null) {
                throw new IllegalArgumentException("userPk is null");
            }
            UserEntity user = userRepository.findById(answerDto.getUserPk())
                    .orElseThrow(() -> new IllegalArgumentException("User does not exist"));*/

            // questionPk 조회
            if (answerDto.getQuestionPk() == null) {
                throw new IllegalArgumentException("questionPk is null");
            }
            SurveyQuestion question = surveyQuestionRepository.findById(answerDto.getQuestionPk())
                    .orElseThrow(() -> new IllegalArgumentException("Question does not exist"));

            // answer가 비면 안됨
            if (answerDto.getAnswer() == null || answerDto.getAnswer().isEmpty()) {
                throw new IllegalArgumentException("Answer cannot be null or empty");
            }

            // 답변 생성
            SurveyAnswer surveyAnswer = new SurveyAnswer();
            surveyAnswer.setEmail(email);
            surveyAnswer.setQuestion(question);
            surveyAnswer.setAnswer(answerDto.getAnswer());
            surveyAnswerRepository.save(surveyAnswer);
        }
    }

    // 설문 결과 조회
    public SurveyResultDto getSurveyResults(Integer surveyPk) {
        // 설문조사 조회
        SurveyEntity survey = surveyRepository.findById(surveyPk)
                .orElseThrow(() -> new IllegalArgumentException("Survey not found"));

        // 해당 설문조사의 질문 리스트 조회
        List<QuestionResultDto> questionResults = surveyQuestionRepository.findBySurvey(survey).stream()
                .map(question -> {
                    // 각 질문에 해당하는 답변 리스트 조회
                    List<String> answers = surveyAnswerRepository.findByQuestion(question).stream()
                            .map(SurveyAnswer::getAnswer)
                            .collect(Collectors.toList());

                    // QuestionResultDto 생성
                    QuestionResultDto questionResultDto = new QuestionResultDto();
                    questionResultDto.setQuestion(question.getQuestion());
                    questionResultDto.setAnswers(answers);
                    return questionResultDto;
                })
                .collect(Collectors.toList());

        // SurveyResultDto 생성
        SurveyResultDto surveyResultDto = new SurveyResultDto();
        surveyResultDto.setTitle(survey.getTitle());
        surveyResultDto.setDescription(survey.getDescription());
        surveyResultDto.setQuestions(questionResults);

        return surveyResultDto;
    }

    // 설문 수정
    public SurveyUpdateResDto updateSurvey(Integer surveyPk, SurveyUpdateReqDto requestDto) {
        // 설문조사 조회
        SurveyEntity survey = surveyRepository.findById(surveyPk)
                .orElseThrow(() -> new EntityNotFoundException("Survey not found"));

        // 수정
        survey.setTitle(requestDto.getTitle());
        survey.setDescription(requestDto.getDescription());
        survey.setEndDate(requestDto.getEndDate());
        surveyRepository.save(survey);

        // 수정 결과 반환
        return new SurveyUpdateResDto(survey);
    }

    // 질문 추가
    public SurveyQuestion addQuestion(Integer surveyPk, QuestionDto questionDto) {
        // 설문조사 조회
        SurveyEntity surveyEntity = surveyRepository.findById(surveyPk)
                .orElseThrow(() -> new IllegalArgumentException("Survey not found"));

        // 질문 추가
        SurveyQuestion newQuestion = new SurveyQuestion();
        newQuestion.setQuestion(questionDto.getQuestion());
        surveyEntity.addQuestion(newQuestion);

        // 설문조사 저장
        surveyRepository.save(surveyEntity);
        return newQuestion;
    }

    // 질문 수정
    public QuestionUpdateResDto updateQuestion(Integer surveyPk, Integer questionPk, QuestionDto questionDto) {
        // 설문조사 조회
        SurveyEntity survey = surveyRepository.findById(surveyPk)
                .orElseThrow(() -> new EntityNotFoundException("Survey not found with id " + surveyPk));

        // 해당 질문이 존재하는지 확인
        SurveyQuestion question = surveyQuestionRepository.findById(questionPk)
                .orElseThrow(() -> new EntityNotFoundException("Question not found with id " + questionPk));

        // 질문 수정
        question.setQuestion(questionDto.getQuestion());

        // 질문 저장
        SurveyQuestion updatedQuestion = surveyQuestionRepository.save(question);

        // 수정된 질문을 DTO로 변환하여 반환
        return new QuestionUpdateResDto(updatedQuestion);
    }

    // 질문 삭제
    public void deleteQuestion(Integer surveyPk, Integer questionPk) {
        // 설문조사 조회
        SurveyEntity survey = surveyRepository.findById(surveyPk)
                .orElseThrow(() -> new IllegalArgumentException("Survey not found"));

        // 해당 질문이 존재하는지 확인
        SurveyQuestion question = surveyQuestionRepository.findById(questionPk)
                .orElseThrow(() -> new IllegalArgumentException("Question not found"));

        // 질문을 삭제
        surveyQuestionRepository.delete(question);
    }
    // 설문 삭제
    public void deleteSurvey(Integer surveyPk) {
        // 설문조사 조회
        SurveyEntity survey = surveyRepository.findById(surveyPk)
                .orElseThrow(() -> new IllegalArgumentException("Survey not found"));

        surveyRepository.delete(survey);
    }
}

