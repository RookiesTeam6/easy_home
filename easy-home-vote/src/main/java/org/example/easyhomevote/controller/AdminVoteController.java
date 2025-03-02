package org.example.easyhomevote.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.easyhomevote.dto.*;
import org.example.easyhomevote.entity.VoteEntity;
import org.example.easyhomevote.entity.VoteOption;
import org.example.easyhomevote.eventdto.*;
import org.example.easyhomevote.kafka.KafkaProducer;
import org.example.easyhomevote.repository.VoteOptionRepository;
import org.example.easyhomevote.repository.VoteRepository;
import org.example.easyhomevote.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/admin/vote")
@PreAuthorize("hasRole('ADMIN')")       // 관리자만 접근 가능
public class AdminVoteController {
    @Autowired
    private VoteService voteService;
    @Autowired
    private KafkaProducer kafkaProducer;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private VoteOptionRepository voteOptionRepository;

    // 투표 등록 - 관리자
    @PostMapping("")
    public ResponseEntity<String> createVote(@RequestHeader("X-Auth-User") String email,
                                             @RequestBody VoteDto voteDto) {
        voteService.createVote(email, voteDto);

        VoteEventDto voteEventDto = VoteEventDto.builder()
                .eventType("CREATE")
                .newVoteTitle(voteDto.getTitle())
                .voteDescription(voteDto.getDescription())
                .options(voteDto.getOptions())
                .build();

        try {
            // 카프카로 설문 생성 이벤트 전송
            kafkaProducer.sendVoteCreateEvent("admin-vote-events", voteEventDto);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(500).body("투표 이벤트 전송 실패");
        }

        return ResponseEntity.ok("투표 생성 완료");
    }

    // 투표 결과 조회 - 관리자, 입주민
    @GetMapping("/{votePk}")
    public VoteResultDto getVoteResults(@PathVariable Integer votePk) {
        return voteService.getVoteResults(votePk);
    }

    // 투표 목록 조회
    @GetMapping("/list")
    public ResponseEntity<List<VoteListDto>> getVoteList() {
        List<VoteListDto> votes = voteService.getAllVotes();
        return ResponseEntity.ok(votes);
    }

    // 투표 수정
    @PutMapping("/{votePk}")
    public ResponseEntity<VoteUpdateResDto> updateVote(
            @PathVariable Integer votePk,
            @RequestBody VoteUpdateReqDto requestDto) {
        VoteUpdateResDto updatedVote = voteService.updateVote(votePk, requestDto);

        // 투표 수정 이벤트
        VoteUpdateEventDto voteUpdateEventDto = VoteUpdateEventDto.builder()
                .eventType("UPDATE")
                .updatedVotePk(updatedVote.getVotePk())
                .title(updatedVote.getTitle())
                .description(updatedVote.getDescription())
                .endDate(updatedVote.getEndDate())
                .build();

        try{
            kafkaProducer.sendVoteUpdateEvent("admin-vote-events", voteUpdateEventDto);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "투표 이벤트 전송 실패", e);
        }

        return ResponseEntity.ok(updatedVote);
    }

    // 투표 삭제
    @DeleteMapping("/{votePk}")
    public ResponseEntity<String> deleteVote(@PathVariable Integer votePk) {
        // 투표 삭제 이벤트
        VoteEntity deletedvote = voteRepository.findById(votePk)
                .orElseThrow(() -> new IllegalArgumentException("Vote not found"));

        VoteDeleteEventDto voteDeleteEventDto = VoteDeleteEventDto.builder()
                .eventType("DELETE")
                .deletedVotePk(deletedvote.getVotePk())
                .title(deletedvote.getTitle())
                .build();

        voteService.deleteVote(votePk);

        try{
            kafkaProducer.sendVoteDeleteEvent("admin-vote-events", voteDeleteEventDto);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "투표 이벤트 전송 실패", e);
        }

        return ResponseEntity.ok("투표 삭제 완료");
    }

    // 선택지 추가
    @PostMapping("/{votePk}/option")
    public ResponseEntity<String> addOption(@PathVariable Integer votePk,
                                              @RequestBody OptionDto optionDto) {
        VoteOption newOption = voteService.addOption(votePk, optionDto);

        VoteEntity vote = voteRepository.findById(votePk)
                .orElseThrow(() -> new IllegalArgumentException("Vote not found"));

        // 선택지 추가 이벤트
        OptionEventDto optionEventDto = OptionEventDto.builder()
                .eventType("UPDATE")
                .votePk(votePk)
                .voteTitle(vote.getTitle())
                .newOption(newOption.getContent())
                .build();

        try{
            kafkaProducer.sendOptionCreateEvent("admin-vote-events", optionEventDto);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "투표 이벤트 전송 실패", e);
        }

        return ResponseEntity.ok("선택지 추가 완료");
    }

    // 선택지 수정
    @PutMapping("/{votePk}/option/{optionPk}")
    public ResponseEntity<OptionUpdateResDto> updateOption(@PathVariable Integer votePk,
                                                               @PathVariable Integer optionPk,
                                                               @RequestBody OptionDto optionDto) {
        OptionUpdateResDto updatedOptionDto = voteService.updateOption(votePk, optionPk, optionDto);

        VoteEntity updatedVote = voteRepository.findById(votePk)
                .orElseThrow(() -> new IllegalArgumentException("Vote not found"));
        VoteOption updatedOption = voteOptionRepository.findById(optionPk)
                .orElseThrow(() -> new IllegalArgumentException("Option not found"));

        // 선택지 수정 이벤트
        OptionUpdateEventDto optionUpdateEventDto = OptionUpdateEventDto.builder()
                .eventType("UPDATE")
                .updatedOptionPk(optionPk)
                .option(updatedOption.getContent())
                .votePk(votePk)
                .voteTitle(updatedVote.getTitle())
                .build();

        try{
            kafkaProducer.sendOptionUpdateEvent("admin-vote-events", optionUpdateEventDto);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "투표 이벤트 전송 실패", e);
        }

        return ResponseEntity.ok(updatedOptionDto);
    }

    // 선택지 삭제
    @DeleteMapping("/{votePk}/option/{optionPk}")
    public ResponseEntity<String> deleteOption(@PathVariable Integer votePk,
                                                 @PathVariable Integer optionPk) {

        VoteEntity vote = voteRepository.findById(votePk)
                .orElseThrow(() -> new IllegalArgumentException("Vote not found"));
        VoteOption deletedOption = voteOptionRepository.findById(optionPk)
                .orElseThrow(() -> new IllegalArgumentException("Option not found"));

        // 선택지 삭제 이벤트
        OptionDeleteEventDto optionDeleteEventDto = OptionDeleteEventDto.builder()
                .eventType("UPDATE")
                .deletedOptionPk(optionPk)
                .content(deletedOption.getContent())
                .votePk(votePk)
                .voteTitle(vote.getTitle())
                .build();

        voteService.deleteOption(votePk, optionPk);

        try{
            kafkaProducer.sendOptionDeleteEvent("admin-vote-events", optionDeleteEventDto);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "투표 이벤트 전송 실패", e);
        }

        return ResponseEntity.ok("선택지 삭제 완료");
    }
}
