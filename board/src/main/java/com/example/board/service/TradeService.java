package com.example.board.service;

import com.example.board.entity.TradeEntity;
import com.example.board.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TradeService {

    private final TradeRepository tradeRepository;

    // 게시글 작성
    public TradeEntity createTrade(TradeEntity trade) {
        trade.setCreatedDate(LocalDateTime.now());
        return tradeRepository.save(trade);
    }

    // 게시글 수정
    public TradeEntity updateTrade(Integer id, TradeEntity tradeDetails) {
        TradeEntity trade = tradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        trade.setTitle(tradeDetails.getTitle());
        trade.setContent(tradeDetails.getContent());
        trade.setAuthor(tradeDetails.getAuthor());
        trade.setPrice(tradeDetails.getPrice());
        trade.setModifiedDate(LocalDateTime.now());
        return tradeRepository.save(trade);
    }

    // 게시글 삭제
    public void deleteTrade(Integer id) {
        tradeRepository.deleteById(id);
    }

    // 개별 게시물 조회
    public TradeEntity getTrade(Integer id) {
        return tradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    // 모든 게시물 조회
    public List<TradeEntity> getAllTrades() {
        return tradeRepository.findAll();
    }



}
