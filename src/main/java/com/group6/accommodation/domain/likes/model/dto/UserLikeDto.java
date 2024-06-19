package com.group6.accommodation.domain.likes.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import com.group6.accommodation.domain.likes.model.entity.UserLikeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserLikeDto {

    public UserEntity user;
    public AccommodationEntity accommodation;

    public static UserLikeDto toDto (UserLikeEntity userLikeEntity) {
        return UserLikeDto.builder()
            .user(userLikeEntity.getUser())
            .accommodation(userLikeEntity.getAccommodation())
            .build()
            ;
    }
}
