package com.javamentor.qa.platform.models.dto;

import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Rustam
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDto {
    private Long id;
    private Long userId;
    private Long userReputation;
    private String image;
    private String nickName;
    private Long questionId;
    private String body;
    private LocalDateTime persistDate;
    private Boolean isHelpful;
    private LocalDateTime dateAccept;
    private Long countValuable;
}