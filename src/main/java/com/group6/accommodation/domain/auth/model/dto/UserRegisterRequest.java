package com.group6.accommodation.domain.auth.model.dto;

import lombok.Getter;

@Getter
public class UserRegisterRequest {
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
}
