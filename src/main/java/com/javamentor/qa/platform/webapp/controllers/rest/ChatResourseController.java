package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.IncomeMessageDTO;
import com.javamentor.qa.platform.models.dto.OutcomeMessageDTO;
import com.javamentor.qa.platform.models.entity.chat.Chat;
import com.javamentor.qa.platform.models.entity.chat.Message;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.ChatService;
import com.javamentor.qa.platform.service.abstracts.model.MessageService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import com.javamentor.qa.platform.webapp.converters.MessageConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ChatResourseController {

//    @Autowired
    private final UserService userService;
//    private GroupChatService groupChatService;
    private final ChatService chatService;
    private final MessageService messageService;
    private final MessageConverter messageConverter;


    @GetMapping("/registered")
    public ResponseEntity<User> getregisterUser(Principal principal) {
        User registerUser = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        System.out.println(registerUser);
        return new ResponseEntity<>(registerUser, HttpStatus.OK);
    }


    @MessageMapping("/user/chat/{chatId}/register")
    @SendTo("/user/chat/{chatId}")
    public OutcomeMessageDTO register(@Payload IncomeMessageDTO incomeMessageDTO,
                                      SimpMessageHeaderAccessor headerAccessor,
                                      @DestinationVariable("chatId") Long chatId) {
        Optional<Chat> optionalChat = chatService.getById(chatId);
        Message message = new Message();
        User user = new User();
        if (optionalChat.isPresent()) {
            Principal principal1 = headerAccessor.getUser();
            Optional<User> optionalUser = userService.getByEmail(principal1.getName());
            if (optionalUser.isPresent()) {
                user = optionalUser.get();
                message.setMessage("JOIN");
                message.setUserSender(user);
                message.setChat(optionalChat.get());
                messageService.persist(message);
            }
        }
        return MessageConverter.INSTANCE.toOutcomeMessageDTO(message, user);
    }


    @MessageMapping("/user/chat/{chatId}/send")
    @SendTo("/user/chat/{chatId}")
    public OutcomeMessageDTO sendMessage(@Payload IncomeMessageDTO incomeMessageDTO,
                                         SimpMessageHeaderAccessor headerAccessor,
                                         @DestinationVariable("chatId") Long chatId) {
        Optional<Chat> optionalChat = chatService.getById(chatId);
        Message message = new Message();
        User user = new User();
        if (optionalChat.isPresent()) {
            Principal principal1 = headerAccessor.getUser();
            Optional<User> optionalUser = userService.getByEmail(principal1.getName());
            if (optionalUser.isPresent()) {
                user = optionalUser.get();
                message.setMessage(incomeMessageDTO.getMessageContent());
                message.setUserSender(user);
                message.setChat(optionalChat.get());
                messageService.persist(message);
            }
        }
        return MessageConverter.INSTANCE.toOutcomeMessageDTO(message, user);
    }


}
