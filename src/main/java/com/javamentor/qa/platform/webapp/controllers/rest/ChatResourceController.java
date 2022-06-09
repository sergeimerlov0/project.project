package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.service.abstracts.dto.ChatDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.PaginationServiceDto;
import com.javamentor.qa.platform.service.impl.dto.PaginationServiceDtoImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user/chat")
public class ChatResourceController {

    private final ChatDtoService chatDtoService;

    @GetMapping("{id}/single/message")
    public ResponseEntity<?> getPaginationMessagesSortedDate
            (@RequestParam(defaultValue = "10") int itemsOnPage, @RequestParam int currentPageNumber
                    , @PathVariable long id) {

        Map<String, Object> paginationMap = new HashMap<>();
        paginationMap.put("class", "MessageDtoSortedByDate");
        paginationMap.put("userId", id);
        paginationMap.put("itemsOnPage", itemsOnPage);
        paginationMap.put("currentPageNumber", currentPageNumber);
        return ResponseEntity.ok(chatDtoService.getPageDto(itemsOnPage, currentPageNumber, paginationMap));
    }
}
