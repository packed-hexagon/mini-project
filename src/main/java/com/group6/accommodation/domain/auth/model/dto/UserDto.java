package com.group6.accommodation.domain.auth.model.dto;

import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import com.group6.accommodation.global.exception.error.AuthErrorCode;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long userId;
    private String email;
    private String password;
    private String name;
    private String phoneNumber;

    public static UserDto toDto(UserEntity user) {
        return UserDto.builder()
                .email(user.getEmail())
                .password(user.getEncryptedPassword())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
