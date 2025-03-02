package org.example.easyhomevote.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="vote_option")
@Data
@NoArgsConstructor
public class VoteOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer optionPk;

    @ManyToOne
    @JoinColumn(name="vote_pk", nullable=false)
    private VoteEntity vote;

    private String content;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer count = 0;

    // 투표참여
    @OneToMany(mappedBy = "option", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VoteAnswer> answers;
}
