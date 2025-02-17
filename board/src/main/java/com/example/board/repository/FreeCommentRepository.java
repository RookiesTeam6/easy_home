package com.example.board.repository;

import com.example.board.entity.FreeCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreeCommentRepository extends JpaRepository<FreeCommentEntity, Integer> {
}
