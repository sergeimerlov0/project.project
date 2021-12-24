package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.RoleService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import com.javamentor.qa.platform.service.impl.model.MailService;
import com.javamentor.qa.platform.service.impl.model.UserServiceImpl;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Rustam
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("api/invite")
@Api(value = "Поучение сообщения с паролем для регистрации на платформе", tags = {"Регистрация на платформе"})
public class InviteController {

    private final MailService mailService;
    private final UserService userService;
    private final RoleService roleService;

    @GetMapping()
    public void getInvite(@RequestParam String email) {
        String password = userService.generateRandomPassword();
        String message = String.format("Hello, %s! Your password for logging system - '%s'. " +
                "Don't forgot change your password after logging system", email, password);
        User user = new User(null, email, password, email,
                null, null, null, null, null,
                null, null, null, null, null,
                null, roleService.getById(2L).get());

        userService.persist(user);
        mailService.send(email, "registration in Kata StackOverFlow", message);

    }

}
