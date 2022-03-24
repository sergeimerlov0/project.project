package com.javamentor.qa.platform.models.dto;

import lombok.*;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Rustam
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelatedTagsDto {
    Long id;
    String title;
    Integer countQuestion;
}