package com.example.board.repository;

import com.example.board.entity.ComplaintEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintRepository extends JpaRepository<ComplaintEntity, Integer> {

}
