package com.javamentor.qa.platform.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupChatDto {
    private Long id;
    private String chatName;
    private PageDto<MessageDto> pageOfMessageDto;
    private LocalDateTime persistDateTime;

    public GroupChatDto(Long id, String chatName, LocalDateTime persistDateTime) {
        this.id = id;
        this.chatName = chatName;
        this.persistDateTime = persistDateTime;
    }
}
