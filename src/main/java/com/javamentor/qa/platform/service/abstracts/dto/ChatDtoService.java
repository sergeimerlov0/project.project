package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.MessageDto;
import com.javamentor.qa.platform.models.dto.QuestionCommentDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.entity.chat.Message;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ChatDtoService extends PaginationServiceDto<MessageDto> {

//    List<MessageDto> getMessagePage(Map<String, Object> param);
//    List<MessageDto> getItems(Map<String, Object> param);
}