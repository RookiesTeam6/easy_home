package com.example.board.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ComplaintCommentDto {
    private Integer Id;
    private String content;
    private String author;
    private Integer postId;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

}
