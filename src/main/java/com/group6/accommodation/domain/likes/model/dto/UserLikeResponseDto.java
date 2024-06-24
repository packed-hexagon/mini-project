package com.group6.accommodation.domain.likes.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import com.group6.accommodation.domain.likes.model.entity.UserLikeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserLikeResponseDto {

    public UserEntity user;
    public AccommodationEntity accommodation;

    public static UserLikeResponseDto toDto (UserLikeEntity userLikeEntity) {
        return UserLikeResponseDto.builder()
            .user(userLikeEntity.getUser())
            .accommodation(userLikeEntity.getAccommodation())
            .build()
            ;
    }
}