package com.example.board.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FreeDto {
    private Integer id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

}
