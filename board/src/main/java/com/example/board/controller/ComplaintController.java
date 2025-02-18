package com.example.board.controller;

import com.example.board.dto.ComplaintDto;
import com.example.board.entity.ComplaintEntity;
import com.example.board.kafka.KafkaProducer;
import com.example.board.service.ComplaintService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/board/complaint")
public class ComplaintController {

    private final ComplaintService complaintService;
    private final KafkaProducer kafkaProducer;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    public ComplaintEntity createComplaint(@RequestHeader("Authorization") String accessToken, @RequestBody ComplaintEntity complaint) {
        // ComplaintEntity를 ComplaintDto로 변환
        ComplaintDto complaintDto = new ComplaintDto();
        complaintDto.setId(complaint.getId());
        complaintDto.setTitle(complaint.getTitle());
        complaintDto.setContent(complaint.getContent());
        complaintDto.setAuthor(complaint.getAuthor());
        complaintDto.setCreatedDate(complaint.getCreatedDate());
        complaintDto.setModifiedDate(complaint.getModifiedDate());

        // 민원 게시글을 데이터베이스에 저장
        ComplaintEntity savedComplaint = complaintService.createComplaint(complaint);

        // Kafka를 통해 관리자에게 알림 전송
        try {
            kafkaProducer.sendMsg("complaints-topic", complaintDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return savedComplaint;
    }

//    // 카프카 테스트
//    @PreAuthorize("hasRole('USER')")
//    @PostMapping("/toadmin")
//    public ResponseEntity<String> notifyAdmin(@RequestHeader("Authorization") String accessToken, @RequestBody ComplaintEntity complaint) {
//        complaint.setAuthor(accessToken);
//        kafkaProducer.sendMsg("민원처리부탁드립니다.", new ComplaintDto());
//        return ResponseEntity.ok("민원 요청 완료");
//    }

    // 게시글 수정
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    public ComplaintEntity updateComplaint(@RequestHeader("Authorization") String accessToken, @PathVariable Integer id, @RequestBody ComplaintEntity complaint) {
        return complaintService.updateComplaint(id, complaint);
    }

    // 게시글 삭제
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComplaint(@RequestHeader("Authorization") String accessToken, @PathVariable Integer id) {
        complaintService.deletecomplaint(id);
        return ResponseEntity.noContent().build();
    }

    // 개별 게시물 조회
    @GetMapping("/{id}")
    public ComplaintEntity getPost(@PathVariable Integer id) {
        return complaintService.getComplaint(id);
    }

    // 모든 게시물 조회
    @GetMapping("")
    public List<ComplaintEntity> getAllComplaints() {
        return complaintService.getAllcomplaints();
    }


}
