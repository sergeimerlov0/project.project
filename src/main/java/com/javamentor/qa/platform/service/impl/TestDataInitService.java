package com.javamentor.qa.platform.service.impl;

import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.RoleService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class TestDataInitService {

    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public TestDataInitService(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    private void init() {
        // изменить при необходимости
        roleService.persist(new Role("ADMIN"));
        roleService.persist(new Role("USER"));

        User admin = new User();
        admin.setRole(roleService.getById(1L).get());
        admin.setEmail("Vasya@mail.ru");
        admin.setPassword("password");
        admin.setFullName("Vasya");
        admin.setCity("Vasya's City");
        admin.setLinkSite("vasya.ru");
        admin.setLinkGitHub("github.com/vasya");
        admin.setLinkVk("vk.com/vasya");
        admin.setAbout("Hello my name is Vasya");
        admin.setImageLink("vasya.ru/myphoto/1");
        admin.setNickname("Vasya1337");
        userService.persist(admin);

        User user = new User();
        user.setRole(roleService.getById(2L).get());
        user.setEmail("Oleg@mail.ru");
        user.setPassword("password");
        user.setFullName("Oleg");
        user.setCity("Oleg's City");
        user.setLinkSite("oleg.ru");
        user.setLinkGitHub("github.com/oleg");
        user.setLinkVk("vk.com/oleg");
        user.setAbout("Hello my name is Oleg");
        user.setImageLink("Oleg.ru/myphoto/1");
        user.setNickname("Oleg1337");
        userService.persist(user);
    }
}
