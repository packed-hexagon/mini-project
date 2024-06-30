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
public class UserResponseDto {
    private Long userId;
    private String email;
    private String name;
    private String phoneNumber;

    public static UserResponseDto toResponse(UserEntity userEntity) {
        return UserResponseDto.builder()
                .userId(userEntity.getId())
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .phoneNumber(userEntity.getPhoneNumber())
                .build();
    }
}
