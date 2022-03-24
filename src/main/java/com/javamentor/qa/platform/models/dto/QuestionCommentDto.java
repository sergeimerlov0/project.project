package com.javamentor.qa.platform.models.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionCommentDto {
    private Long id;
    private Long questionId;
    private LocalDateTime lastRedactionDate;
    private LocalDateTime persistDate;
    private String text;
    private Long userId;
    private String imageLink;
    private Long reputation;
}