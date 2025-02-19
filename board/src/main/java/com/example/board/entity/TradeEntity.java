package com.example.board.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@EntityListeners(EntityListeners.class)
@Entity
@Data
public class TradeEntity extends BaseTimeEntity {

    private String title;
    private String content;
    private String author;
    private String price;

    @OneToMany(mappedBy = "trade", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TradeCommentEntity> tradecomments;
}
