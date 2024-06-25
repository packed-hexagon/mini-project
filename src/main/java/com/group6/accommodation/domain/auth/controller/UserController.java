package com.group6.accommodation.domain.auth.controller;

import com.group6.accommodation.domain.auth.model.dto.UserRegisterRequestDto;
import com.group6.accommodation.domain.auth.model.dto.UserRegisterResponseDto;
import com.group6.accommodation.domain.auth.service.UserService;
import com.group6.accommodation.global.security.service.CustomUserDetails;
import com.group6.accommodation.global.util.ResponseApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserController {

    private final UserService userService;

    @GetMapping("/api/user")
    public ResponseEntity<ResponseApi<UserRegisterResponseDto>> getUserInfo(
            @AuthenticationPrincipal CustomUserDetails user
            ) {
        ResponseApi<UserRegisterResponseDto> response = userService.getUserInfo(user.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/open-api/user/register")
    public ResponseEntity<ResponseApi<UserRegisterResponseDto>> register(
            @Valid
            @RequestBody UserRegisterRequestDto request
    ) {
        ResponseApi<UserRegisterResponseDto> response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
