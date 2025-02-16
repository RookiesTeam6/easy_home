package com.example.board.controller;

import com.example.board.dto.ComplaintCommentDto;
import com.example.board.entity.ComplaintCommentEntity;
import com.example.board.entity.ComplaintEntity;

import com.example.board.service.ComplaintCommentService;
import com.example.board.service.ComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/board/complaint-comment")
public class ComplaintCommentController {

    private final ComplaintCommentService complaintCommentService;
    private final ComplaintService complaintService;

    // 댓글 작성
    @PostMapping("complaint/{complaintId}")  // URL 매핑 변경
    public ComplaintCommentEntity createComplaintComment(@PathVariable Integer complaintId, @RequestBody ComplaintCommentDto complaintCommentDto) {  // 메소드 이름 및 반환 타입 변경
        ComplaintEntity complaint = complaintService.getComplaint(complaintId);  // ComplaintEntity 조회
        return complaintCommentService.createComplaintComment(complaint, complaintCommentDto.getAuthor(), complaintCommentDto.getContent());  // 메소드 이름 변경
    }

    // 댓글 수정
    @PutMapping("/{id}")
    public ComplaintCommentEntity updateComplaintComment(@PathVariable Integer id, @RequestBody ComplaintCommentDto complaintCommentDto) {  // 메소드 이름 및 반환 타입 변경
        return complaintCommentService.updateComplaintComment(id, complaintCommentDto.getAuthor(), complaintCommentDto.getContent());  // 메소드 이름 변경
    }

    // 댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComplaintComment(@PathVariable Integer id) {
        complaintCommentService.deletecomplaintComment(id);
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
