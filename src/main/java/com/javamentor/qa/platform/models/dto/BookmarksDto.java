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
public class BookmarksDto {
    private Long id;
    private Long questionId;
//    private String title;
//    private Long userId;
//    private List<TagDto> listTagDto;
//    private Long countAnswer;
//    private Long countVote;
//    private Long countView;
    private LocalDateTime persistDate;


    public BookmarksDto(Long id, Long questionId, LocalDateTime persistDate) {
        this.id = id;
        this.questionId = questionId;
//        this.title = title;
//        this.countAnswer = countAnswer;
        this.persistDate = persistDate;
    }
}
