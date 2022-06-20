package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.ChatDtoDao;
import com.javamentor.qa.platform.models.dto.SingleChatDto;
import com.javamentor.qa.platform.service.abstracts.dto.ChatDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatDtoServiceImpl implements ChatDtoService {
    private final ChatDtoDao chatDtoDao;

    @Override
    public List<SingleChatDto> getAllSingleChatDto(Long id) {
        return chatDtoDao.getAllSingleChatDto(id);
    }
}
