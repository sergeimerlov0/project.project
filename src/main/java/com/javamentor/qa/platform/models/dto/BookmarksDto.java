package com.javamentor.qa.platform.models.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BookmarksDto {
    private Long id;
    private Long questionId;
    private LocalDateTime persistDate;


    public BookmarksDto(Long id, Long questionId, LocalDateTime persistDate) {
        this.id = id;
        this.questionId = questionId;
        this.persistDate = persistDate;
    }
}
