package com.example.board.controller;

import com.example.board.entity.FreeEntity;
import com.example.board.entity.TradeEntity;
import com.example.board.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/board/trade")
public class TradeController {

    private final TradeService tradeService;

    // 게시글 작성
    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    public TradeEntity createTrade(@RequestHeader("Authorization") String accessToken, @RequestBody TradeEntity trade) {
        return tradeService.createTrade(trade);
    }

    // 게시글 수정
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    public TradeEntity updateTrade(@RequestHeader("Authorization") String accessToken, @PathVariable Integer id, @RequestBody TradeEntity trade) {
        return tradeService.updateTrade(id, trade);
    }

    // 게시글 삭제
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrade(@RequestHeader("Authorization") String accessToken, @PathVariable Integer id) {
        tradeService.deleteTrade(id);
        return ResponseEntity.noContent().build();
    }

    // 개별 게시물 조회
    @GetMapping("/{id}")
    public TradeEntity getTrade(@PathVariable Integer id) {
        return tradeService.getTrade(id);
    }

//    // 모든 게시물 조회
//    @GetMapping("")
//    public List<TradeEntity> getAllTrades() {
//        return tradeService.getAllTrades();
//    }

    // 페이징 기능
    @GetMapping("")
    public ResponseEntity<Page<TradeEntity>> getPosts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<TradeEntity> paging = this.tradeService.getPosts(page, size);
        return ResponseEntity.ok(paging);       // 반환할 뷰로 수정
    }
}