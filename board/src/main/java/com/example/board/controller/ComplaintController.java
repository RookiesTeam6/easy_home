package com.example.board.controller;

import com.example.board.dto.ComplaintDto;
import com.example.board.entity.ComplaintEntity;
import com.example.board.entity.FreeEntity;
import com.example.board.kafka.KafkaProducer;
import com.example.board.service.ComplaintService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public String createComplaint(@RequestHeader("Authorization") String accessToken, @RequestBody ComplaintEntity complaint) {
        // ComplaintEntity를 ComplaintDto로 변환
        ComplaintDto complaintDto = new ComplaintDto();
        complaintDto.setTitle(complaint.getTitle());
        complaintDto.setContent(complaint.getContent());
        ComplaintEntity savedComplaint = complaintService.createComplaint(complaint);

        // Kafka를 통해 ADMIN에게 알림 전송
        try {
            kafkaProducer.sendMsg("Board-complaints-topic", complaintDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "등록 실패"; // 예외 발생 시 실패 메시지 반환
        }
        return "등록 완료"; // 성공적으로 전송된 경우 메시지 반환
    }

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

//    // 모든 게시물 조회
//    @GetMapping("")
//    public List<ComplaintEntity> getAllComplaints() {
//        return complaintService.getAllcomplaints();
//    }

    // 페이징 기능
    @GetMapping("")
    public ResponseEntity<Page<ComplaintEntity>> getPosts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<ComplaintEntity> paging = this.complaintService.getPosts(page, size);
        return ResponseEntity.ok(paging);       // 반환할 뷰로 수정
    }
}