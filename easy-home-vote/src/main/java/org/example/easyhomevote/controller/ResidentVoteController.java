package org.example.easyhomevote.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.easyhomevote.dto.*;
import org.example.easyhomevote.entity.VoteEntity;
import org.example.easyhomevote.eventdto.JoinEventDto;
import org.example.easyhomevote.kafka.KafkaProducer;
import org.example.easyhomevote.repository.VoteOptionRepository;
import org.example.easyhomevote.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/resident/vote")
@PreAuthorize("hasRole('USER')")       // 입주민만 접근 가능
public class ResidentVoteController {
    @Autowired
    private VoteService voteService;
    @Autowired
    private VoteOptionRepository voteOptionRepository;
    @Autowired
    private KafkaProducer kafkaProducer;

    // 투표 참여 - 입주민
    @PostMapping("/join")
    public ResponseEntity<String> joinVote(@RequestHeader("X-Auth-User") String email,
                                           @RequestBody AnswerDto answerDto) {
        voteService.joinVote(email, answerDto);

        // Optional로 VoteEntity 조회
        Optional<VoteEntity> voteOptional = voteOptionRepository.findVoteByOptionPk(answerDto.getOptionPk());

        // VoteEntity가 없으면 에러 반환
        if (voteOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("투표를 찾을 수 없습니다.");
        }

        VoteEntity vote = voteOptional.get();

        JoinEventDto joinEventDto = JoinEventDto.builder()
                .joinedVotePk(vote.getVotePk())
                .voteTitle(vote.getTitle())
                .build();

        try {
            kafkaProducer.sendJoinEvent("resident-vote-events", joinEventDto);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(500).body("투표 이벤트 전송 실패");
        }

        return ResponseEntity.ok("투표 참여 완료");
    }

    // 투표 결과 조회 - 관리자, 입주민
    @GetMapping("/{votePk}")
    public VoteResultDto getVoteResults(@PathVariable Integer votePk) {
        return voteService.getVoteResults(votePk);
    }
}
