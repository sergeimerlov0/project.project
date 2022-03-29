package com.javamentor.qa.platform.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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