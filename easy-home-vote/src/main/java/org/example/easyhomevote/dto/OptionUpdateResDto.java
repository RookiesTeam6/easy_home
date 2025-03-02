package org.example.easyhomevote.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.easyhomevote.entity.VoteOption;

@Data
@ToString
@NoArgsConstructor
public class OptionUpdateResDto {
    private Integer optionPk;
    private String content;

    // 투표 정보
    private Integer votePk;
    private String voteTitle;

    // 엔티티를 dto로 변환
    public OptionUpdateResDto(VoteOption option) {
        this.optionPk = option.getOptionPk();
        this.content = option.getContent();
        this.votePk = option.getVote().getVotePk();
        this.voteTitle = option.getVote().getTitle();
    }
}