package com.group6.accommodation.domain.auth.model.dto;

import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserDto {
    private Long userId;
    private String email;
    private String password;
    private String name;
    private String phoneNumber;

    public static UserDto toDto(UserEntity user) {
        return UserDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .password(user.getEncryptedPassword())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
