package com.example.board.controller;

import com.example.board.entity.FreeEntity;
import com.example.board.entity.NotificationEntity;
import com.example.board.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/notification/notice")
public class NotificationController {

    private final NotificationService notificationService;

    // 게시글 작성
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public NotificationEntity createPost(@RequestHeader("Authorization") String accessToken, @RequestBody NotificationEntity post) {
        return notificationService.createPost(post);
    }

    // 게시글 수정
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public NotificationEntity updatePost(@RequestHeader("Authorization") String accessToken, @PathVariable Integer id, @RequestBody NotificationEntity post) {
        return notificationService.updatePost(id, post);
    }

    // 게시글 삭제
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@RequestHeader("Authorization") String accessToken, @PathVariable Integer id) {
        notificationService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    // 개별 게시물 조회
    @GetMapping("/{id}")
    public NotificationEntity getPost(@PathVariable Integer id) {
        return notificationService.getPost(id);
    }

//    // 모든 게시물 조회
//    @GetMapping("")
//    public List<NotificationEntity> getAllPosts() {
//        return notificationService.getAllPosts();
//    }

    // 페이징 기능
    @GetMapping("")
    public ResponseEntity<Page<NotificationEntity>> getPosts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<NotificationEntity> paging = this.notificationService.getPosts(page, size);
        return ResponseEntity.ok(paging);       // 반환할 뷰로 수정
    }
}