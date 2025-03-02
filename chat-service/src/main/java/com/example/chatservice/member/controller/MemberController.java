package com.example.chatservice.member.controller;

import com.example.chatservice.member.dto.MemberResponse;
import com.example.chatservice.member.dto.SimpleMemberResponse;
import com.example.chatservice.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/resident/chat/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/{userPk}")
    public ResponseEntity<MemberResponse> getMember(@PathVariable Long userPk) {
        return ResponseEntity.ok(memberService.getMember(userPk));
    }

    @GetMapping
    public ResponseEntity<Map<String, List<SimpleMemberResponse>>> getMembersGroupedByAddress() {
        return ResponseEntity.ok(memberService.getMembersGroupedByAddress());
    }

    @GetMapping("/address/{address}")
    public ResponseEntity<List<MemberResponse>> getMembersByAddress(@PathVariable String address) {
        return ResponseEntity.ok(memberService.getMembersByAddress(address));
    }

//    @GetMapping("/grouped-by-address")
//    public ResponseEntity<Map<String, List<SimpleMemberResponse>>> getMembersGroupedByAddress() {
//        return ResponseEntity.ok(memberService.getMembersGroupedByAddress());
//    }
}