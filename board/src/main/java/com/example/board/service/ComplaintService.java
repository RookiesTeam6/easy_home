package com.example.board.service;

import com.example.board.entity.ComplaintEntity;
import com.example.board.entity.FreeEntity;
import com.example.board.repository.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ComplaintService {

    private final ComplaintRepository complaintRepository;

    // 게시글 작성
    public ComplaintEntity createComplaint(ComplaintEntity complaint) {
        complaint.setCreatedDate(LocalDateTime.now());
        return complaintRepository.save(complaint);
    }

    // 게시글 수정
    public ComplaintEntity updateComplaint(Integer id, ComplaintEntity complaintDetails) {
        ComplaintEntity complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        complaint.setTitle(complaintDetails.getTitle());
        complaint.setContent(complaintDetails.getContent());
        complaint.setAuthor(complaintDetails.getAuthor());
        complaint.setModifiedDate(LocalDateTime.now());
        return complaintRepository.save(complaint);
    }

    // 게시글 삭제
    public void deletecomplaint(Integer id) {
        complaintRepository.deleteById(id);
    }

    // 개별 게시물 조회
    public ComplaintEntity getComplaint(Integer id) {
        return complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

//    // 모든 게시물 조회
//    public List<ComplaintEntity> getAllcomplaints() {
//        return complaintRepository.findAll();
//    }

    // 페이징 기능
    public Page<ComplaintEntity> getPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, 10); // 페이지 크기를 10으로 고정
        return complaintRepository.findAll(pageable);
    }

}

