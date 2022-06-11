package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.MessageDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/chat")
public class ChatResourceController {

    private final MessageDtoService messageDtoService;

    @GetMapping("/{id}/single/message")
    public ResponseEntity<?> getPaginationMessagesSortedDate
            (@RequestParam(defaultValue = "10") int itemsOnPage, @RequestParam int currentPageNumber
                    , @PathVariable long id) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();

        Map<String, Object> paginationMap = new HashMap<>();
        paginationMap.put("class", "MessageDtoSortedByDate");
        paginationMap.put("chatId", id);
        paginationMap.put("itemsOnPage", itemsOnPage);
        paginationMap.put("currentPageNumber", currentPageNumber);
        paginationMap.put("userId", user.getId());

        return ResponseEntity.ok(messageDtoService.getPageDto(itemsOnPage, currentPageNumber, paginationMap).getItems());
    }
}