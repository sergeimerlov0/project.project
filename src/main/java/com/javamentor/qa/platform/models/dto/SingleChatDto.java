package com.javamentor.qa.platform.models.dto;

import com.javamentor.qa.platform.models.entity.chat.Chat;
import com.javamentor.qa.platform.models.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SingleChatDto {
    private Long id;
    private String name;
    private String image;
    private String lastMessage;
    private LocalDateTime persistDateTimeLastMessage;
}
