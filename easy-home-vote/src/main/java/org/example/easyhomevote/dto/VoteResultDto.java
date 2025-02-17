package org.example.easyhomevote.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class VoteResultDto {
    private String title;
    private String description;
    private List<OptionResultDto> options;
}
