package com.example.board.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class ComplaintCommentEntity extends BaseTimeEntity {

    @Column(nullable = false)
    private String content;

    private String author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private ComplaintEntity complaint;

}
