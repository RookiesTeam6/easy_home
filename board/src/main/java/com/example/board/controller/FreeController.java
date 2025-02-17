package com.example.board.controller;

import com.example.board.entity.FreeEntity;
import com.example.board.service.FreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/board/free")
public class FreeController {

    private final FreeService freeService;

    // 게시글 작성
    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    public FreeEntity createPost(@RequestHeader("Authorization") String accessToken, @RequestBody FreeEntity post) {
        return freeService.createPost(post);
    }

    // 게시글 수정
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    public FreeEntity updatePost(@RequestHeader("Authorization") String accessToken, @PathVariable Integer id, @RequestBody FreeEntity post) {
        return freeService.updatePost(id, post);
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer id) {
        freeService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    // 개별 게시물 조회
    @GetMapping("/{id}")
    public FreeEntity getPost(@PathVariable Integer id) {
        return freeService.getPost(id);
    }

    // 모든 게시물 조회
    @GetMapping("")
    public List<FreeEntity> getAllPosts() {
        return freeService.getAllPosts();
    }
}
