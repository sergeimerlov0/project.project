package com.javamentor.qa.platform.models.dto;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String email;
    private String password;
}