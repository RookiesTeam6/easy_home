package org.example.easyhomevote.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.easyhomevote.dto.*;
import org.example.easyhomevote.entity.*;
import org.example.easyhomevote.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoteService {
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private VoteOptionRepository voteOptionRepository;
    @Autowired
    private VoteAnswerRepository voteAnswerRepository;

    // 투표 등록 - 관리자
    public void createVote(String email, VoteDto voteDto) {
        /*// 관리자번호가 없거나 존재하지 않으면 안됨 (인증 후 수정)
        if (voteDto.getManagerPk() == null) {
            throw new IllegalArgumentException("ManagerPk is null");
        }
        ManagerEntity manager = managerRepository.findById(voteDto.getManagerPk())
                .orElseThrow(() -> new IllegalArgumentException("Manager does not exist"));*/

        // title이 비면 안됨
        if (voteDto.getTitle() == null || voteDto.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        // title 중복 여부 확인
        if (voteRepository.findByTitle(voteDto.getTitle()).isPresent()) {
            throw new IllegalArgumentException("Title already exists");
        }

        // 투표 생성
        VoteEntity voteEntity = new VoteEntity();
        voteEntity.setEmail(email);
        voteEntity.setTitle(voteDto.getTitle());
        voteEntity.setDescription(voteDto.getDescription());
        voteEntity.setEndDate(voteDto.getEndDate());
        voteRepository.save(voteEntity);

        // 선택지 리스트 저장
        List<OptionDto> options = voteDto.getOptions();
        // 최소 1개의 선택지가 있어야함
        if (options == null || options.isEmpty()) {
            throw new IllegalArgumentException("At least one option must be provided");
        }
        for (OptionDto optionDto : options) {
            VoteOption voteOption = new VoteOption();
            voteOption.setContent(optionDto.getContent());
            voteOption.setVote(voteEntity);
            voteOptionRepository.save(voteOption);
        }
    }
    // 투표 참여 - 입주민
    public void joinVote(String email, AnswerDto answerDto) {
        /*// 회원번호 조회
        if (answerDto.getUserPk() == null) {
            throw new IllegalArgumentException("userPk is null");
        }
        UserEntity user = userRepository.findById(answerDto.getUserPk())
                .orElseThrow(() -> new IllegalArgumentException("User does not exist"));*/

        // votePk 조회
        if (answerDto.getVotePk() == null) {
            throw new IllegalArgumentException("votePk is null");
        }
        VoteEntity vote = voteRepository.findById(answerDto.getVotePk())
                .orElseThrow(() -> new IllegalArgumentException("Vote does not exist"));

        // optionPk 조회
        if (answerDto.getOptionPk() == null) {
            throw new IllegalArgumentException("optionPk is null");
        }
        VoteOption option = voteOptionRepository.findById(answerDto.getOptionPk())
                .orElseThrow(() -> new IllegalArgumentException("Option does not exist"));

        // 입력된 votePk와 option의 votePk가 동일한지 확인
        if (!option.getVote().getVotePk().equals(vote.getVotePk())) {
            throw new IllegalArgumentException("Option does not belong to the specified vote.");
        }

        // 같은 투표에 이미 참여했는지 확인
        boolean alreadyVoted = voteAnswerRepository.existsByEmailAndVote(email, vote);
        if (alreadyVoted) {
            throw new IllegalStateException("User can vote only once in this poll.");
        }

        // 답변 생성
        VoteAnswer voteAnswer = new VoteAnswer();
        voteAnswer.setEmail(email);
        voteAnswer.setVote(vote);
        voteAnswer.setOption(option);
        voteAnswerRepository.save(voteAnswer);

        // option 테이블의 count 1 증가
        option.setCount(option.getCount() + 1);
        voteOptionRepository.save(option);
    }

    // 투표 결과 조회
    public VoteResultDto getVoteResults(Integer votePk) {
        // 투표 조회
        VoteEntity vote = voteRepository.findById(votePk)
                .orElseThrow(() -> new IllegalArgumentException("Vote not found"));

        // 해당 투표의 선택지 리스트 조회
        List<OptionResultDto> optionResults = voteOptionRepository.findByVote(vote).stream()
                .map(option -> {
                    // OptionResultDto 생성
                    OptionResultDto optionResultDto = new OptionResultDto();
                    optionResultDto.setOption(option.getContent());
                    optionResultDto.setCount(option.getCount());
                    return optionResultDto;
                })
                .collect(Collectors.toList());

        // voteResultDto 생성
        VoteResultDto voteResultDto = new VoteResultDto();
        voteResultDto.setTitle(vote.getTitle());
        voteResultDto.setDescription(vote.getDescription());
        voteResultDto.setOptions(optionResults);

        return voteResultDto;
    }

    // 투표 수정
    public VoteUpdateResDto updateVote(Integer votePk, VoteUpdateReqDto requestDto) {
        // 투표 조회
        VoteEntity vote = voteRepository.findById(votePk)
                .orElseThrow(() -> new IllegalArgumentException("Vote not found"));

        // 수정
        vote.setTitle(requestDto.getTitle());
        vote.setDescription(requestDto.getDescription());
        vote.setEndDate(requestDto.getEndDate());
        voteRepository.save(vote);

        // 수정 결과 반환
        return new VoteUpdateResDto(vote);
    }

    // 투표 삭제
    public void deleteVote(Integer votePk) {
        // 투표 조회
        VoteEntity vote = voteRepository.findById(votePk)
                .orElseThrow(() -> new IllegalArgumentException("Vote not found"));

        voteRepository.delete(vote);
    }

    // 선택지 추가
    public void addOption(Integer votePk, OptionDto optionDto) {
        // 투표 조회
        VoteEntity voteEntity = voteRepository.findById(votePk)
                .orElseThrow(() -> new IllegalArgumentException("Vote not found"));

        // 선택지 추가
        VoteOption newOption = new VoteOption();
        newOption.setContent(optionDto.getContent());
        voteEntity.addOption(newOption);

        // 투표 저장
        voteRepository.save(voteEntity);
    }

    // 선택지 수정
    public OptionUpdateResDto updateOption(Integer votePk, Integer optionPk, OptionDto optionDto) {
        // 투표 조회
        VoteEntity vote = voteRepository.findById(votePk)
                .orElseThrow(() -> new EntityNotFoundException("Vote not found with id " + votePk));

        // 해당 선택지가 존재하는지 확인
        VoteOption option = voteOptionRepository.findById(optionPk)
                .orElseThrow(() -> new EntityNotFoundException("Option not found with id " + optionPk));

        // 입력된 votePk와 option의 votePk가 동일한지 확인
        if (!option.getVote().getVotePk().equals(vote.getVotePk())) {
            throw new IllegalArgumentException("Option does not belong to the specified vote.");
        }

        // 선택지 수정
        option.setContent(optionDto.getContent());

        // 선택지 저장
        VoteOption updatedOption = voteOptionRepository.save(option);

        // 수정된 선택지를 DTO로 변환하여 반환
        return new OptionUpdateResDto(updatedOption);
    }

    // 선택지 삭제
    public void deleteOption(Integer votePk, Integer optionPk) {
        // 투표 조회
        VoteEntity vote = voteRepository.findById(votePk)
                .orElseThrow(() -> new EntityNotFoundException("Vote not found with id " + votePk));

        // 해당 선택지가 존재하는지 확인
        VoteOption option = voteOptionRepository.findById(optionPk)
                .orElseThrow(() -> new EntityNotFoundException("Option not found with id " + optionPk));

        // 입력된 votePk와 option의 votePk가 동일한지 확인
        if (!option.getVote().getVotePk().equals(vote.getVotePk())) {
            throw new IllegalArgumentException("Option does not belong to the specified vote.");
        }

        // 선택지 삭제
        voteOptionRepository.delete(option);
    }
}