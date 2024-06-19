package com.group6.accommodation.domain.auth.controller;

import com.group6.accommodation.domain.auth.model.dto.UserDto;
import com.group6.accommodation.domain.auth.model.dto.UserRegisterRequest;
import com.group6.accommodation.domain.auth.model.dto.UserRegisterResponse;
import com.group6.accommodation.domain.auth.service.UserService;
import com.group6.accommodation.global.util.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @GetMapping
    public void test() {
    }

    @PostMapping("/test")
    public void post() {
        
    }

    @PostMapping
    public void register(
            @RequestBody UserRegisterRequest request
    ) {
        userService.register(request);
    }
}
