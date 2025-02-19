package com.example.board.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TradeDto {
    private Integer Id;
    private String title;
    private String content;
    private String author;
    private String price;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

}
