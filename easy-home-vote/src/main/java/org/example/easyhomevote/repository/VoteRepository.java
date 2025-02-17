package org.example.easyhomevote.repository;

import org.example.easyhomevote.entity.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<VoteEntity, Integer> {
    Optional<VoteEntity> findByTitle(String title);
}
