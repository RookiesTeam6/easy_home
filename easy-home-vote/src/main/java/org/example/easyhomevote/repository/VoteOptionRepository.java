package org.example.easyhomevote.repository;

import org.example.easyhomevote.entity.VoteEntity;
import org.example.easyhomevote.entity.VoteOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteOptionRepository extends JpaRepository<VoteOption, Integer> {
    List<VoteOption> findByVote(VoteEntity vote);
}
