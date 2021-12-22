package com.javamentor.qa.platform.models.dto;

import lombok.*;

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
