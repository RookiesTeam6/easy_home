package org.example.easyhomevote.repository;

import org.example.easyhomevote.entity.VoteEntity;
import org.example.easyhomevote.entity.VoteOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VoteOptionRepository extends JpaRepository<VoteOption, Integer> {
    List<VoteOption> findByVote(VoteEntity vote);

    @Query("SELECT v FROM VoteEntity v JOIN VoteOption o ON v.votePk = o.vote.votePk WHERE o.optionPk = :optionPk")
    Optional<VoteEntity> findVoteByOptionPk(@Param("optionPk") Integer optionPk);
}
