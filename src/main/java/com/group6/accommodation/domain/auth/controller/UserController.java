package com.group6.accommodation.domain.auth.controller;

import com.group6.accommodation.domain.auth.model.dto.UserDto;
import com.group6.accommodation.domain.auth.model.dto.UserRegisterRequest;
import com.group6.accommodation.domain.auth.service.UserService;
import com.group6.accommodation.global.util.ResponseApi;
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

    @PostMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.status(HttpStatus.CREATED).body("Resource created successfully");
    }

    @PostMapping("/open-api/user/register")
    public ResponseApi<UserDto> register(
            @RequestBody UserRegisterRequest request
    ) {
        UserDto response = userService.register(request);
        return ResponseApi.success(HttpStatus.CREATED, response);
    }
}
