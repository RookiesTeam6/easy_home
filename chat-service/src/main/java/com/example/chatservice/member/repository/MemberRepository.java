package com.example.chatservice.member.repository;

import com.example.chatservice.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByNickname(String nickname);
    Optional<Member> findByNickname(String nickname);
    List<Member> findByAddress(String address);
    List<Member> findByAddressOrderByNicknameAsc(String address);
}