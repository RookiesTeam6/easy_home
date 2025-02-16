package com.example.chatservice.member.config;

import com.example.chatservice.member.entity.Member;
import com.example.chatservice.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class InitialDataConfig {
    private final MemberRepository memberRepository;
    private final JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        if (memberRepository.count() == 0) {
            // easy_home_user.members 테이블에서 username 조회
            List<String> usernames = jdbcTemplate.queryForList(
                    "SELECT username FROM easy_home_user.members",
                    String.class
            );

            // 조 회한 username으로 Member 엔티티 생성 및 저장
            List<Member> members = usernames.stream()
                    .map(username -> Member.builder()
                            .nickname(username)
                            .build())
                    .collect(Collectors.toList());

            memberRepository.saveAll(members);
        }
    }
}