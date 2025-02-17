package com.example.board.service;

import com.example.board.entity.FreeCommentEntity;
import com.example.board.entity.FreeEntity;
import com.example.board.entity.TradeCommentEntity;
import com.example.board.entity.TradeEntity;
import com.example.board.repository.FreeCommentRepository;
import com.example.board.repository.TradeCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class TradeCommentService {

    private final TradeCommentRepository tradeCommentRepository;

    // 댓글 작성
    public TradeCommentEntity createComment(TradeEntity post, String author, String content) {
        TradeCommentEntity comment = new TradeCommentEntity();
        comment.setContent(content);
        comment.setAuthor(author);
        comment.setCreatedDate(LocalDateTime.now());
        comment.setTrade(post);
        return tradeCommentRepository.save(comment);
    }

    // 댓글 수정
    public TradeCommentEntity updateComment(Integer id, String author, String content) {
        TradeCommentEntity comment = tradeCommentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setAuthor(author);
        comment.setContent(content);
        comment.setModifiedDate(LocalDateTime.now());
        return tradeCommentRepository.save(comment);
    }

    // 댓글 삭제
    public void deleteComment(Integer id) {
        tradeCommentRepository.deleteById(id);
    }

//    // 개별 댓글 조회
//    public CommentEntity getComment(Integer id) {
//        return commentRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Comment not found"));
//    }
//
//    // 모든 댓글 조회
//    public List<CommentEntity> getAllComments() {
//        return commentRepository.findAll();
//    }
}