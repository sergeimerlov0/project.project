package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.SingleChatDto;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.ChatDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.MessageDtoService;
import com.javamentor.qa.platform.models.dto.GroupChatDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/chat")
public class ChatResourceController {
    private final ChatDtoService chatDtoService;
    private final MessageDtoService messageDtoService;

    @GetMapping("/single")
    public ResponseEntity<List<SingleChatDto>> getAllOfSingleChatDto(Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        return  ResponseEntity.ok().body(chatDtoService.getAllSingleChatDto(id));
    }

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
    @GetMapping("/group")
    @ApiOperation(value = "?????????????????? ???????? MessageDto ?? ???????????????????? ?? ?????????????????????? ???? ?????????????? ??????????????",
            tags = {"Get Sorted by time MessageDto"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "?????? MessageDto ????????????????"),
            @ApiResponse(code = 400, message = "MessageDto ???? ??????????????")
    })
    public ResponseEntity<?> getAllSortedByDateMessageDto(@RequestParam(defaultValue = "30") int itemsOnPage,
                                                          @RequestParam int currentPageNumber,
                                                          @RequestParam Long chatId) {

        Optional<GroupChatDto> o = chatDtoService.getGroupChatByIdWithPaginationMessage(itemsOnPage, currentPageNumber, chatId);
        if (o.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(o.get());
    }
}