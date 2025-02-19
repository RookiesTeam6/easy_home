package com.example.board.service;

import com.example.board.entity.FreeCommentEntity;
import com.example.board.entity.FreeEntity;
import com.example.board.repository.FreeCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class FreeCommentService {

    private final FreeCommentRepository freeCommentRepository;

    // 댓글 작성
    public FreeCommentEntity createComment(FreeEntity post, String author, String content) {
        FreeCommentEntity comment = new FreeCommentEntity();
        comment.setContent(content);
        comment.setAuthor(author);
        comment.setCreatedDate(LocalDateTime.now());
        comment.setFree(post);
        return freeCommentRepository.save(comment);
    }

    // 댓글 수정
    public FreeCommentEntity updateComment(Integer id, String author, String content) {
        FreeCommentEntity comment = freeCommentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setAuthor(author);
        comment.setContent(content);
        comment.setModifiedDate(LocalDateTime.now());
        return freeCommentRepository.save(comment);
    }

    // 댓글 삭제
    public void deleteComment(Integer id) {
        freeCommentRepository.deleteById(id);
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