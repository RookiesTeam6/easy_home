package com.example.board.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TradeCommentDto {
    private Integer Id;
    private String content;
    private String author;
    private Integer postId;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

}
