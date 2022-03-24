package com.javamentor.qa.platform.models.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagViewDto {
    private Long id;
    private String title;
    private String description;
    private Long questionCount;
    private Long questionCountOneDay;
    private Long questionCountWeekDay;
}