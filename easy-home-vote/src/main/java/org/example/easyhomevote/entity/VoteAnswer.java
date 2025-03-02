package org.example.easyhomevote.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="vote_answer")
@NoArgsConstructor
public class VoteAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer answerPk;

    @ManyToOne
    @JoinColumn(name="vote_pk", nullable = false)
    private VoteEntity vote;

    @ManyToOne
    @JoinColumn(name="option_pk", nullable=false)
    private VoteOption option;

    private String email; // 입주민 이메일
}
