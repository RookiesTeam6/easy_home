package com.example.board.controller;

import com.example.board.dto.FreeCommentDto;
import com.example.board.dto.TradeCommentDto;
import com.example.board.entity.FreeCommentEntity;
import com.example.board.entity.FreeEntity;
import com.example.board.entity.TradeCommentEntity;
import com.example.board.entity.TradeEntity;
import com.example.board.service.FreeCommentService;
import com.example.board.service.FreeService;
import com.example.board.service.TradeCommentService;
import com.example.board.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/board/trade-comment")
public class TradeCommentController {

    private final TradeCommentService tradeCommentService;
    private final TradeService tradeService;

    // 댓글 작성
    @PostMapping("post/{postId}")
    public TradeCommentEntity createComment(@RequestHeader("Authorization") String accessToken, @PathVariable Integer postId, @RequestBody TradeCommentDto tradeCommentDto) {
        TradeEntity post = tradeService.getTrade(postId);
        return tradeCommentService.createComment(post, tradeCommentDto.getAuthor(), tradeCommentDto.getContent());
    }

    // 댓글 수정
    @PutMapping("/{id}")
    public TradeCommentEntity updateComment(@RequestHeader("Authorization") String accessToken, @PathVariable Integer id, @RequestBody TradeCommentDto tradeCommentDto) {
        return tradeCommentService.updateComment(id, tradeCommentDto.getAuthor(), tradeCommentDto.getContent());
    }

    // 댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@RequestHeader("Authorization") String accessToken, @PathVariable Integer id) {
        tradeCommentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

//    // 개별 댓글 조회
//    @GetMapping("/{id}")
//    public CommentEntity getComment(@PathVariable Integer id) {
//        return commentService.getComment(id);
//    }
//
//    // 모든 댓글 조회
//    @GetMapping("")
//    public List<CommentEntity> getAllComments() {
//        return commentService.getAllComments();
//    }
}
