package com.example.board.controller;

import com.example.board.dto.FreeCommentDto;
import com.example.board.entity.FreeCommentEntity;
import com.example.board.entity.FreeEntity;
import com.example.board.service.FreeCommentService;
import com.example.board.service.FreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/board/comment")
public class FreeCommentController {

    private final FreeCommentService freeCommentService;
    private final FreeService freeService;

    // 댓글 작성
    @PostMapping("free/{postId}")
    public FreeCommentEntity createComment(@PathVariable Integer postId, @RequestBody FreeCommentDto freeCommentDto) {
        FreeEntity post = freeService.getPost(postId);
        return freeCommentService.createComment(post, freeCommentDto.getAuthor(), freeCommentDto.getContent());
    }

    // 댓글 수정
    @PutMapping("/{id}")
    public FreeCommentEntity updateComment(@PathVariable Integer id, @RequestBody FreeCommentDto freeCommentDto) {
        return freeCommentService.updateComment(id, freeCommentDto.getAuthor(), freeCommentDto.getContent());
    }

    // 댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer id) {
        freeCommentService.deleteComment(id);
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
