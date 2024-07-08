package com.group6.accommodation.domain.auth.controller;

import com.group6.accommodation.domain.auth.model.dto.UserRequestDto;
import com.group6.accommodation.domain.auth.model.dto.UserResponseDto;
import com.group6.accommodation.domain.auth.service.UserService;
import com.group6.accommodation.global.security.filter.JwtFilter;
import com.group6.accommodation.global.security.service.CustomUserDetails;
import com.group6.accommodation.global.security.token.model.dto.LoginTokenResponseDto;
import com.group6.accommodation.global.util.ResponseApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
@Tag(name = "User", description = "사용자 관련 API")
public class UserController {

    private final UserService userService;

    @GetMapping("/api/user")
    @Operation(summary = "유저 정보 조회", description = "로그인한 사용자의 정보를 조회합니다.")
    public ResponseEntity<ResponseApi<UserResponseDto>> getUserInfo(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        UserResponseDto result = userService.getUserInfo(user.getUserId());
        ResponseApi<UserResponseDto> response = ResponseApi.success(HttpStatus.OK, result);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/open-api/user/refresh-tokens")
    @Operation(summary = "Token 재발급", description = "AccessToken, RefreshToken을 새로 발급합니다.")
    @Parameters
    public ResponseEntity<ResponseApi<LoginTokenResponseDto>> refreshTokens(
            @RequestHeader(JwtFilter.AUTHORIZATION_HEADER) String accessToken,
            @CookieValue(value = "refreshToken", required = false) String refreshToken
    ) {
        log.info("refresh token: {}", refreshToken);
        LoginTokenResponseDto result = userService.refreshTokens(accessToken, refreshToken);
        ResponseApi<LoginTokenResponseDto> refreshTokens = ResponseApi.success(HttpStatus.OK, result);

        HttpHeaders headers = userService.createRefreshTokenCookie(refreshTokens.getData().getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(refreshTokens);
    }

    @PostMapping("/open-api/user/register")
    @Operation(summary = "회원가입")
    public ResponseEntity<ResponseApi<UserResponseDto>> register(
            @Valid
            @RequestBody UserRequestDto request
    ) {
        UserResponseDto result = userService.register(request);
        ResponseApi<UserResponseDto> response = ResponseApi.success(HttpStatus.CREATED, result);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/api/user/logout")
    @Operation(summary = "로그아웃")

    public ResponseEntity<?> logout(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        HttpHeaders headers = userService.logout(user.getUserId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).headers(headers).build();
    }
}
