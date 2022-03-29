package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.RoleService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import com.javamentor.qa.platform.service.impl.model.MailService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @Value("#{systemProperties['connection.url']}/login")
    String loginEndPoint;

    @GetMapping()
    public void getInvite(@RequestParam String email) {
        String password = userService.generateRandomPassword();
        String message = String.format("Hello, %s! Your password for logging system by address %s - '%s'. " +
                "Don't forgot change your password after logging system", email, loginEndPoint, password);
        User user = new User(email, password, email, roleService.getRoleByName("USER").get());
        userService.persist(user);
        mailService.send(email, "registration in Kata StackOverFlow", message);
    }
}