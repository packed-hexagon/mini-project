package com.group6.accommodation.domain.auth.controller;

import com.group6.accommodation.domain.auth.model.dto.UserRequestDto;
import com.group6.accommodation.domain.auth.model.dto.UserResponseDto;
import com.group6.accommodation.domain.auth.service.UserService;
import com.group6.accommodation.global.security.filter.JwtFilter;
import com.group6.accommodation.global.security.service.CustomUserDetails;
import com.group6.accommodation.global.security.token.model.dto.LoginTokenResponseDto;
import com.group6.accommodation.global.util.ResponseApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserController {

    private final UserService userService;

    @GetMapping("/api/user")
    public ResponseEntity<ResponseApi<UserResponseDto>> getUserInfo(
            @AuthenticationPrincipal CustomUserDetails user
            ) {
        UserResponseDto result = userService.getUserInfo(user.getUserId());
        ResponseApi<UserResponseDto> response = ResponseApi.success(HttpStatus.OK, result);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/open-api/user/refresh-tokens")
    public ResponseEntity<ResponseApi<LoginTokenResponseDto>> refreshTokens(
            @RequestHeader(JwtFilter.AUTHORIZATION_HEADER) String accessToken,
            @CookieValue(value = "refreshToken", required = false) String refreshToken
    ) {
        LoginTokenResponseDto result = userService.refreshTokens(accessToken, refreshToken);
        ResponseApi<LoginTokenResponseDto> refreshTokens = ResponseApi.success(HttpStatus.OK, result);

        HttpHeaders headers = userService.createRefreshTokenCookie(refreshTokens.getData().getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(refreshTokens);
    }

    @PostMapping("/open-api/user/register")
    public ResponseEntity<ResponseApi<UserResponseDto>> register(
            @Valid
            @RequestBody UserRequestDto request
    ) {
        UserResponseDto result = userService.register(request);
        ResponseApi<UserResponseDto> response = ResponseApi.success(HttpStatus.CREATED, result);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/api/user/logout")
    public ResponseEntity<?> logout(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        HttpHeaders headers = userService.logout(user.getUserId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).headers(headers).build();
    }
}
