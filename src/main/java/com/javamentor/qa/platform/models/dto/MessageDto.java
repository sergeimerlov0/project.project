package com.javamentor.qa.platform.models.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private Long id;
    private String message;
    private String nickName;
    private Long userId;
    private Long chatId;
    private String image;
    private LocalDateTime persistDateTime;
}
