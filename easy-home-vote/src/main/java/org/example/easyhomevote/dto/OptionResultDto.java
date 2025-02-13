package org.example.easyhomevote.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class OptionResultDto {
    private String option;
    private Integer count;
}
