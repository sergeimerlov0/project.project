package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.SingleChatDto;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.ChatDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user/chat")

public class ChatResourceController {
    private final ChatDtoService chatDtoService;

    @GetMapping("/single")
    public ResponseEntity<List<SingleChatDto>> getAllOfSingleChatDto() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        return  ResponseEntity.ok().body(chatDtoService.getAllSingleChatDto());
    }

}
