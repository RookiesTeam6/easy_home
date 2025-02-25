package com.example.chatservice.member.service;

import com.example.chatservice.member.dto.MemberResponse;
import com.example.chatservice.member.dto.SimpleMemberResponse;
import com.example.chatservice.member.entity.Member;
import com.example.chatservice.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
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

    public List<MemberResponse> getMembersByAddress(String address) {
        return memberRepository.findByAddressOrderByNicknameAsc(address).stream()
                .map(MemberResponse::from)
                .collect(Collectors.toList());
    }

    public Map<String, List<SimpleMemberResponse>> getMembersGroupedByAddress() {
        List<Member> allMembers = memberRepository.findAll();

        // 주소별로 그룹화하고 간소화된 응답 사용
        return allMembers.stream()
                .collect(Collectors.groupingBy(
                        Member::getAddress,
                        Collectors.mapping(this::toSimpleMemberResponse, Collectors.toList())
                ));
    }

    // 간소화된 응답 객체로 변환 (주소 정보 제외)
    private SimpleMemberResponse toSimpleMemberResponse(Member member) {
        return SimpleMemberResponse.builder()
                .userPk(member.getUserPk())
                .nickname(member.getNickname())
                .build();
    }



}