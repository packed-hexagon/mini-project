package com.group6.accommodation.domain.auth.controller;

import com.group6.accommodation.domain.auth.model.dto.UserRegisterRequestDto;
import com.group6.accommodation.domain.auth.model.dto.UserRegisterResponseDto;
import com.group6.accommodation.domain.auth.service.UserService;
import com.group6.accommodation.global.util.ResponseApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserController {

    private final UserService userService;

    @PostMapping("/open-api/user/register")
    public ResponseEntity<ResponseApi<UserRegisterResponseDto>> register(
            @Valid
            @RequestBody UserRegisterRequestDto request
    ) {
        ResponseApi<UserRegisterResponseDto> response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
