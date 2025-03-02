package com.example.board.repository;

import com.example.board.entity.FreeCommentEntity;
import com.example.board.entity.TradeCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeCommentRepository extends JpaRepository<TradeCommentEntity, Integer> {
}
