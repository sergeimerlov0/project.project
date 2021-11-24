package com.javamentor.qa.platform.service.impl;

import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.RoleService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
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
        addRole();
        addUser();
    }

    private void addRole() {
        // изменить при необходимости
        roleService.persist(new Role("ADMIN"));
        roleService.persist(new Role("USER"));
    }

    private void addUser() {
        // изменить при необходимости
        Role adminRole = roleService.getById(1L).get();
        Role userRole = roleService.getById(2L).get();


        User admin = new User();
        admin.setRole(adminRole);
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

        User admin1 = new User();
        admin1.setRole(adminRole);
        admin1.setEmail("123@mail.ru");
        admin1.setPassword("123");
        admin1.setFullName("Volodya");
        admin1.setCity("Volodya's City");
        admin1.setLinkSite("Volodya.ru");
        admin1.setLinkGitHub("github.com/Volodya");
        admin1.setLinkVk("vk.com/Volodya");
        admin1.setAbout("Hello my name is Volodya");
        admin1.setImageLink("Volodya.ru/myphoto/1");
        admin1.setNickname("Volodya");
        userService.persist(admin1);

        for (int x = 2; x < 51; x++ ) {
            StringBuilder email = new StringBuilder();
            email.append(x).append("user@mail.ru");

            StringBuilder password = new StringBuilder();
            password.append(x).append(111);

            StringBuilder fullName = new StringBuilder();
            fullName.append(x).append("user");

            StringBuilder city = new StringBuilder();
            city.append(x).append("user's City");

            StringBuilder linkSite = new StringBuilder();
            linkSite.append(x).append("user.ru");

            StringBuilder linkGitHub = new StringBuilder();
            linkGitHub.append("github.com/").append(x).append("user");

            StringBuilder linkVk = new StringBuilder();
            linkVk.append("vk.com/").append(x).append("user");

            StringBuilder about = new StringBuilder();
            about.append("Hello my name is ").append(x).append("user");

            StringBuilder imageLink = new StringBuilder();
            imageLink.append(x).append("user").append(".ru/myphoto/1");

            StringBuilder nickname = new StringBuilder();
            nickname.append(x).append("user");

            User u = new User();
            u.setRole(userRole);
            u.setEmail(email.toString());
            u.setPassword(password.toString());
            u.setFullName(fullName.toString());
            u.setCity(city.toString());
            u.setLinkSite(linkSite.toString());
            u.setLinkGitHub(linkGitHub.toString());
            u.setLinkVk(linkVk.toString());
            u.setAbout(about.toString());
            u.setImageLink(imageLink.toString());
            u.setNickname(nickname.toString());

            userService.persist(u);
        }

    }
}
