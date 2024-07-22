package com.group6.accommodation.domain.likes.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import com.group6.accommodation.domain.likes.model.entity.UserLikeEntity;
import java.time.LocalDateTime;
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

    public Long userId;
    public Long accommodationId;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public static UserLikeResponseDto toDto (UserLikeEntity userLikeEntity) {
        return UserLikeResponseDto.builder()
            .userId(userLikeEntity.getUser().getId())
            .accommodationId(userLikeEntity.getAccommodation().getId())
            .createdAt(userLikeEntity.getCreatedAt())
            .updatedAt(LocalDateTime.now())
            .build()
            ;
    }
}
