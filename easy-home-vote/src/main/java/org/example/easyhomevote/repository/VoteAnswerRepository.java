package org.example.easyhomevote.repository;


import org.example.easyhomevote.entity.VoteAnswer;
import org.example.easyhomevote.entity.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VoteAnswerRepository extends JpaRepository<VoteAnswer, Integer> {
    boolean existsByEmailAndVote(String email, VoteEntity vote);
}
