package com.group6.accommodation.global.security.token.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginTokenRequestDto {
    private String email;
    private String password;
}
