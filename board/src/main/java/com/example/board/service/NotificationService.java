package com.example.board.service;

import com.example.board.entity.FreeEntity;
import com.example.board.entity.NotificationEntity;
import com.example.board.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    // 게시글 작성
    public NotificationEntity createPost(NotificationEntity post) {
        post.setCreatedDate(LocalDateTime.now());
        return notificationRepository.save(post);
    }

    // 게시글 수정
    public NotificationEntity updatePost(Integer id, NotificationEntity postDetails) {
        NotificationEntity post = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        post.setTitle(postDetails.getTitle());
        post.setContent(postDetails.getContent());
        post.setAuthor(postDetails.getAuthor());
        post.setModifiedDate(LocalDateTime.now());
        return notificationRepository.save(post);
    }

    // 게시글 삭제
    public void deletePost(Integer id) {
        notificationRepository.deleteById(id);
    }

    // 개별 게시물 조회
    public NotificationEntity getPost(Integer id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

//    // 모든 게시물 조회
//    public List<NotificationEntity> getAllPosts() {
//        return notificationRepository.findAll();
//    }

    // 페이징 기능
    public Page<NotificationEntity> getPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, 10); // 페이지 크기를 10으로 고정
        return notificationRepository.findAll(pageable);
    }

}
