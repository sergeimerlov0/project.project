package com.javamentor.qa.platform.webapp.controllers.dto;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String email;
    private String password;
}
