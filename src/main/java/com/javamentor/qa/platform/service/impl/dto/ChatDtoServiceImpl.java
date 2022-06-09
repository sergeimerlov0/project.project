//package com.javamentor.qa.platform.service.impl.dto;
//
//import com.javamentor.qa.platform.dao.abstracts.dto.ChatDtoDao;
//import com.javamentor.qa.platform.models.dto.MessageDto;
//import com.javamentor.qa.platform.service.abstracts.dto.ChatDtoService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class ChatDtoServiceImpl extends PaginationServiceDtoImpl<MessageDto> implements ChatDtoService {
//    private final ChatDtoDao chatDtoDao;
//
//    @Override
//    public List<MessageDto> getMessagePage(Map<String, Object> param) {
//        return null;
//    }
//
//
////    @Override
////    public List<MessageDto> getItems(Map<String, Object> param) {
////        return chatDtoDao.getItems(param);
////    }
//
////    public PageDto<MessageDto> getPageDto(int currentPageNumber, int itemsOnPage, Map<String, Object> map) {
////        PageDto<MessageDto> pageDto = getPageDto(currentPageNumber, itemsOnPage, map);
////        List<MessageDto> messageDtoList = pageDto.getItems();
////
////
////    }
//}