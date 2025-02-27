package com.example.chatservice.member.config;

import com.example.chatservice.member.entity.Member;
import com.example.chatservice.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class InitialDataConfig {
    private final MemberRepository memberRepository;
    private final JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        if (memberRepository.count() == 0) {
            // address와 username을 함께 조회
            List<Map<String, Object>> usersData = jdbcTemplate.queryForList(
                    "SELECT username, address FROM easy_home_user.members"
            );

// 주소별로 사용자 그룹화
            Map<String, List<String>> usersByAddress = usersData.stream()
                    .collect(Collectors.groupingBy(
                            row -> (String) row.get("address"),
                            Collectors.mapping(
                                    row -> (String) row.get("username"),
                                    Collectors.toList()
                            )
                    ));

            // 그룹화된 데이터를 Member 엔티티로 변환하여 저장
            List<Member> members = usersByAddress.entrySet().stream()
                    .flatMap(entry -> entry.getValue().stream()
                            .map(username -> Member.builder()
                                    .nickname(username)
                                    .address(entry.getKey())  // 여기서 address 값이 설정됩니다
                                    .build()))
                    .collect(Collectors.toList());

            memberRepository.saveAll(members);
        }
    }
}