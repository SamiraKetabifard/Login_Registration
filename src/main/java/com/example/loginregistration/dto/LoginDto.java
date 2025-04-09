package com.example.loginregistration.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {

    private String usernameOrEmail;
    private String password;
}