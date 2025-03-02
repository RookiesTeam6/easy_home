package com.example.board.service;

import com.example.board.entity.ComplaintCommentEntity;
import com.example.board.entity.ComplaintEntity;
import com.example.board.repository.ComplaintCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ComplaintCommentService {

    private final ComplaintCommentRepository complaintCommentRepository;

    // 댓글 작성
    public ComplaintCommentEntity createComplaintComment(ComplaintEntity complaint, String author, String content) {
        ComplaintCommentEntity complaintComment = new ComplaintCommentEntity();
        complaintComment.setContent(content);
        complaintComment.setAuthor(author);
        complaintComment.setCreatedDate(LocalDateTime.now());
        complaintComment.setComplaint(complaint);
        return complaintCommentRepository.save(complaintComment);
    }

    // 댓글 수정
    public ComplaintCommentEntity updateComplaintComment(Integer id, String author, String content) {
        ComplaintCommentEntity complaintComment = complaintCommentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        complaintComment.setAuthor(author);
        complaintComment.setContent(content);
        complaintComment.setModifiedDate(LocalDateTime.now());
        return complaintCommentRepository.save(complaintComment);
    }

    // 댓글 삭제
    public void deletecomplaintComment(Integer id) {
        complaintCommentRepository.deleteById(id);
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