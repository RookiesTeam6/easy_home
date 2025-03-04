package com.example.board.service;

import com.example.board.entity.FreeEntity;
import com.example.board.repository.FreeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FreeService {

    private final FreeRepository freeRepository;

    // 게시글 작성
    public FreeEntity createPost(FreeEntity post) {
        post.setCreatedDate(LocalDateTime.now());
        return freeRepository.save(post);
    }

    // 게시글 수정
    public FreeEntity updatePost(Integer id, FreeEntity postDetails) {
        FreeEntity post = freeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        post.setTitle(postDetails.getTitle());
        post.setContent(postDetails.getContent());
        post.setAuthor(postDetails.getAuthor());
        post.setModifiedDate(LocalDateTime.now());
        return freeRepository.save(post);
    }

    // 게시글 삭제
    public void deletePost(Integer id) {
        freeRepository.deleteById(id);
    }

    // 개별 게시물 조회
    public FreeEntity getPost(Integer id) {
        return freeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

//    // 모든 게시물 조회
//    public List<FreeEntity> getAllPosts() {
//        return freeRepository.findAll();
//    }

    // 페이징 기능
    public Page<FreeEntity> getPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, 10); // 페이지 크기를 10으로 고정
        return freeRepository.findAll(pageable);
    }
}
