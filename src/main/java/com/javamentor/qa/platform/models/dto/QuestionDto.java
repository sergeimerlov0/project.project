package com.javamentor.qa.platform.models.dto;

import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class QuestionDto {
    private Long id;
    private String title;
    private Long authorId;
    private Long authorReputation;
    private String authorName;
    private String authorImage;
    private String description;
    private Long viewCount;                      //пока не считай это поле, как оно будет считаться решим позже, пусть пока будет 0
    private Long countAnswer;
    private Long countValuable;                  //Это голоса за ответ QuestionVote
    private LocalDateTime persistDateTime;
    private LocalDateTime lastUpdateDateTime;
    private List<TagDto> listTagDto;
    private List<CommentDto> comments;
    private VoteType isUserVote;
    private Boolean isUserAnswerVote;

    public QuestionDto() {
    }

    public QuestionDto(Long id, String title, Long authorId, Long authorReputation, String authorName,
                       String authorImage, String description, Long viewCount, Long countAnswer, Long countValuable,
                       LocalDateTime persistDateTime, LocalDateTime lastUpdateDateTime, VoteType isUserVote, Boolean isUserAnswerVote) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.authorReputation = authorReputation;
        this.authorName = authorName;
        this.authorImage = authorImage;
        this.description = description;
        this.viewCount = viewCount;
        this.countAnswer = countAnswer;
        this.countValuable = countValuable;
        this.persistDateTime = persistDateTime;
        this.lastUpdateDateTime = lastUpdateDateTime;
        this.isUserVote = isUserVote;
        this.isUserAnswerVote = isUserAnswerVote;
    }
}