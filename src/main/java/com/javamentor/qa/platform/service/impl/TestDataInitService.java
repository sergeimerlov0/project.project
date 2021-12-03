package com.javamentor.qa.platform.service.impl;

import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.RoleService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class TestDataInitService {

    private final RoleService roleService;
    private final UserService userService;
    private final Flyway flyway;

    @Autowired
    public TestDataInitService(UserService userService, RoleService roleService, Flyway flyway) {
        this.userService = userService;
        this.roleService = roleService;
        this.flyway = flyway;
    }

    @PostConstruct
    private void init() {
        flyway.clean();
        flyway.migrate();
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

        StringBuilder email = new StringBuilder();
        StringBuilder password = new StringBuilder();
        StringBuilder fullName = new StringBuilder();
        StringBuilder city = new StringBuilder();
        StringBuilder linkSite = new StringBuilder();
        StringBuilder linkGitHub = new StringBuilder();
        StringBuilder linkVk = new StringBuilder();
        StringBuilder about = new StringBuilder();
        StringBuilder imageLink = new StringBuilder();
        StringBuilder nickname = new StringBuilder();

        User admin = new User();
        admin.setRole(adminRole);
        admin.setEmail(email.append("Vasya@mail.ru").toString());
        admin.setPassword(password.append("password").toString());
        admin.setFullName(fullName.append("Vasya").toString());
        admin.setCity(city.append("Vasya's City").toString());
        admin.setLinkSite(linkSite.append("vasya.ru").toString());
        admin.setLinkGitHub(linkGitHub.append("github.com/vasya").toString());
        admin.setLinkVk(linkVk.append("vk.com/vasya").toString());
        admin.setAbout(about.append("Hello my name is Vasya").toString());
        admin.setImageLink(imageLink.append("vasya.ru/myphoto/1").toString());
        admin.setNickname(nickname.append("Vasya1337").toString());
        userService.persist(admin);

        User admin1 = new User();
        admin1.setRole(adminRole);
        admin1.setEmail(email.delete(0, email.length()).append("123@mail.ru").toString());
        admin1.setPassword(password.delete(0, password.length()).append("123").toString());
        admin1.setFullName(fullName.delete(0, fullName.length()).append("Volodya").toString());
        admin1.setCity(city.delete(0, city.length()).append("Volodya's City").toString());
        admin1.setLinkSite(linkSite.delete(0, linkSite.length()).append("Volodya.ru").toString());
        admin1.setLinkGitHub(linkGitHub.delete(0, linkGitHub.length()).append("github.com/Volodya").toString());
        admin1.setLinkVk(linkVk.delete(0, linkVk.length()).append("github.com/Volodya").toString());
        admin1.setAbout(about.delete(0, about.length()).append("Hello my name is Volodya").toString());
        admin1.setImageLink(imageLink.delete(0, imageLink.length()).append("Volodya.ru/myphoto/1").toString());
        admin1.setNickname(nickname.delete(0, nickname.length()).append("Vova").toString());
        userService.persist(admin1);

        for (int x = 2; x < 51; x++ ) {

            email.delete(0, email.length()).append(x).append("user@mail.ru");
            password.delete(0, password.length()).append(x).append(111);
            fullName.delete(0, fullName.length()).append(x).append("user");
            city.delete(0, city.length()).append(x).append("user's City");
            linkSite.delete(0, linkSite.length()).append(x).append("user.ru");
            linkGitHub.delete(0, linkGitHub.length()).append("github.com/").append(x).append("user");
            linkVk.delete(0, linkVk.length()).append("vk.com/").append(x).append("user");
            about.delete(0, about.length()).append("Hello my name is ").append(x).append("user");
            imageLink.delete(0, imageLink.length()).append(x).append("user").append(".ru/myphoto/1");
            nickname.delete(0, nickname.length()).append(x).append("user");

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
