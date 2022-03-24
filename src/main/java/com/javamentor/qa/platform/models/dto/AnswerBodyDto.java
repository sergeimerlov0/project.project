package com.javamentor.qa.platform.models.dto;

import lombok.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerBodyDto {
    @NotBlank
    String htmlBody;
}