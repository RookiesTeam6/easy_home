package org.example.easyhomevote.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class OptionResultDto {
    private Integer optionPk;
    private String option;
    private Integer count;
}
