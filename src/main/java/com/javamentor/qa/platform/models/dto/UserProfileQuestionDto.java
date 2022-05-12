package com.javamentor.qa.platform.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileQuestionDto {
    private Long questionId;
    private String title;
    private List<TagDto> tagList;
    private Long countAnswer;
    private LocalDateTime persistDate;

    public UserProfileQuestionDto(Long questionId, String title, Long countAnswer, LocalDateTime persistDate) {
        this.questionId = questionId;
        this.title = title;
        this.countAnswer = countAnswer;
        this.persistDate = persistDate;
    }
}