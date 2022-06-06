package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.service.abstracts.dto.PaginationServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user/chat")
public class ChatResourceController {
    private final PaginationServiceDto paginationServiceDto;

    public ResponseEntity<?> getPaginationMessageSortDate(@RequestParam(defaultValue = "10") int itemsOnPage
                                                    , @RequestParam int currentPageNumber) {
        Map<String, Object> paginationMap = new HashMap<>();
        paginationMap.put("class", "PaginationMessageSortDate");
        paginationMap.put("itemsOnPage", itemsOnPage);
        paginationMap.put("currentPageNumber", currentPageNumber);
        return ResponseEntity.ok(paginationServiceDto.getMessageDto(itemsOnPage, currentPageNumber, paginationMap));
    }
}
