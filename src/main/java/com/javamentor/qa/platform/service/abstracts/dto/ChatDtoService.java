package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.SingleChatDto;

import java.util.List;
import com.javamentor.qa.platform.models.dto.GroupChatDto;

import java.util.Optional;

public interface ChatDtoService {
    List<SingleChatDto> getAllSingleChatDto(Long id);
    Optional<GroupChatDto> getGroupChatByIdWithPaginationMessage(int itemsOnPage, int currentPageNumber,Long chatId);

}
