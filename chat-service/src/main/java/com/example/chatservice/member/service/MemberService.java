package com.example.chatservice.member.service;

import com.example.chatservice.member.dto.MemberResponse;
import com.example.chatservice.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberResponse getMember(Long userPk) {
        return memberRepository.findById(userPk)
                .map(MemberResponse::from)
                .orElseThrow(() -> new RuntimeException("Member not found"));
    }

    public List<MemberResponse> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::from)
                .collect(Collectors.toList());
    }
}