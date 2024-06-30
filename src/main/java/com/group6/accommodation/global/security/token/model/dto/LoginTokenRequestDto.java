package com.group6.accommodation.global.security.token.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginTokenRequestDto {
    private String email;
    private String password;
}
