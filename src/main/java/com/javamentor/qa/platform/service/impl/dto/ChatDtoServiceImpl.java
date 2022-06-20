package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.ChatDtoDao;
import com.javamentor.qa.platform.models.dto.GroupChatDto;
import com.javamentor.qa.platform.service.abstracts.dto.ChatDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.MessageDtoService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ChatDtoServiceImpl implements ChatDtoService {

    private final ChatDtoDao chatDtoDao;
    private final MessageDtoService messageDtoService;

    public ChatDtoServiceImpl(ChatDtoDao chatDtoDao, MessageDtoService messageDtoService) {
        this.chatDtoDao = chatDtoDao;
        this.messageDtoService = messageDtoService;
    }

    @Override
    public Optional<GroupChatDto> getGroupChatByIdWithPaginationMessage(int itemsOnPage,
                                                                        int currentPageNumber,
                                                                        Long chatId) {
        Optional<GroupChatDto> o = chatDtoDao.getGroupChatByChatId(chatId);
        if (o.isEmpty()) {
            return Optional.empty();
        }
        Map<String, Object> paginationMap = new HashMap<>();
        paginationMap.put("class", "SortedByDateMessageDto");
        paginationMap.put("currentPageNumber", currentPageNumber);
        paginationMap.put("itemsOnPage", itemsOnPage);
        paginationMap.put("chatId", chatId);
        GroupChatDto group = o.get();
        group.setPageOfMessageDto(messageDtoService.getPageDto(currentPageNumber, itemsOnPage, paginationMap));
        return Optional.of(group);
    }
}
