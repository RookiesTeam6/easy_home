package com.example.board.repository;

import com.example.board.entity.FreeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreeRepository extends JpaRepository<FreeEntity, Integer> {
}
