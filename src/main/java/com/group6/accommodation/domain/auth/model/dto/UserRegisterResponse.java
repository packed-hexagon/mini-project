package com.group6.accommodation.domain.auth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterResponse {
    private Long userId;
    private String email;
    private String name;
    private String phoneNumber;
}
