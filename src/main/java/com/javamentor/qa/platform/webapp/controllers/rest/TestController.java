package com.javamentor.qa.platform.webapp.controllers.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test")
@Api(value = "Endpoints for Retrieving of Test String List.", tags = {"List String"})
public class TestController {

    @ApiOperation(value = "API to GET of Test String List", notes = "Get all string list", tags = {"TestString"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success retrieve", response = List.class)
    })
    @GetMapping
    public List<String> getTest() {
        return new ArrayList<>();
    }

    /*
    Just Security Test
     */
    @GetMapping("/user")
    public String user(){
        return "hi, user!";
    }
    @GetMapping("/admin")
    public String admin(){
        return "hi, admin!";
    }
}
