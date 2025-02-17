package com.example.chatservice.member.config;

import com.example.chatservice.member.entity.Member;
import com.example.chatservice.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class MemberSyncConfig {
    private final MemberRepository memberRepository;
    private final JdbcTemplate jdbcTemplate;

    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void syncMembers() {
        List<String> usernames = jdbcTemplate.queryForList(
                "SELECT username FROM easy_home_user.members",
                String.class
        );

        // 새로운 사용자 추가 로직
        for (String username : usernames) {
            if (!memberRepository.existsByNickname(username)) {
                memberRepository.save(Member.builder()
                        .nickname(username)
                        .build());
            }
        }
    }
}