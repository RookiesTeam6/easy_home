package org.example.easyhomevote.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class VoteListDto {
    private Integer votePk;
    private String title;
}
