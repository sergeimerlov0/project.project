package com.javamentor.qa.platform.webapp.controllers;

import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.dao.impl.dto.pagination.RegUserImpl;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//http://localhost:8080/api/user/users?page=1&items=20
@Controller
@RequestMapping("/api")
public class TestClass {

//    private final UserDtoService userDtoService;
//
//    public TestClass(UserDtoService userDtoService) {
//        this.userDtoService = userDtoService;
//    }

//    private final UserService userService;
//
//    public TestClass(UserService userService) {
//        this.userService = userService;
//    }
    private final RegUserImpl regUser;

    public TestClass(RegUserImpl regUser) {
        this.regUser = regUser;
    }

    @GetMapping("/user/users")
    public String getAllUsers(Model model, @RequestParam("page") int page, @RequestParam("items") int items){
        Map<String, Object> param = new HashMap<>();
        param.put("page", page);
        param.put("items", items);

        List<UserDto> userList = regUser.getItems(param);
        int pages = regUser.getTotalResultCount(param)/items;
        if(regUser.getTotalResultCount(param)%items!=0){
            pages++;
        }
        model.addAttribute("users", userList);
        model.addAttribute("items", items);
        if (page + 1 <= pages){
            model.addAttribute("nPage", page + 1);
        } else {
            model.addAttribute("nPage", page );
        }

        if (page - 1 > 0){
            model.addAttribute("pPage", page - 1);
        } else {
            model.addAttribute("pPage", page );
        }
        return "test2";
    }
    @GetMapping("/user/users1")
    public String getUsers(){
        return "user";
    }

}
