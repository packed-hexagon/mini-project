package com.group6.accommodation.global.security.token.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginTokenResponseDto {
    private String accessToken;
    private String refreshToken;
}
