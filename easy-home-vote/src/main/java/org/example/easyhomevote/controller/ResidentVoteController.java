package org.example.easyhomevote.controller;

import org.example.easyhomevote.dto.*;
import org.example.easyhomevote.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resident/vote")
@PreAuthorize("hasRole('USER')")       // 입주민만 접근 가능
public class ResidentVoteController {
    @Autowired
    private VoteService voteService;

    // 투표 참여 - 입주민
    @PostMapping("/join")
    public ResponseEntity<String> joinVote(@RequestHeader("X-Auth-User") String email,
                                           @RequestBody AnswerDto answerDto) {
        voteService.joinVote(email, answerDto);
        return ResponseEntity.ok("투표 참여 완료");
    }

    // 투표 결과 조회 - 관리자, 입주민
    @GetMapping("/{votePk}")
    public VoteResultDto getVoteResults(@PathVariable Integer votePk) {
        return voteService.getVoteResults(votePk);
    }
}
