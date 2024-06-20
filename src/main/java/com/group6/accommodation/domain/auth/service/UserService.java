package com.group6.accommodation.domain.auth.service;

import com.group6.accommodation.domain.auth.model.dto.UserDto;
import com.group6.accommodation.domain.auth.model.dto.UserRegisterRequest;
import com.group6.accommodation.domain.auth.model.dto.UserRegisterResponse;
import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import com.group6.accommodation.domain.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto register(UserRegisterRequest request) {
        log.info("User Registered : {}", request.getName());
        return UserDto.toDto(userRepository.save(UserEntity.builder()
                .email(request.getEmail())
                .encryptedPassword(request.getPassword())
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .build())
        );
    }

}
