package org.example.msasbuser.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ComplaintDto {
    private Integer id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
