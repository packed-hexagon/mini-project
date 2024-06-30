package com.group6.accommodation.domain.likes.model.dto;

import static org.junit.jupiter.api.Assertions.*;

import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import com.group6.accommodation.domain.likes.model.entity.UserLikeEntity;
import com.group6.accommodation.domain.likes.model.entity.UserLikeId;
import org.junit.jupiter.api.Test;

public class UserLikeResponseDtoTest {

    @Test
    public void testToDto() {
        UserEntity user = new UserEntity();
        user.setId(1L);

        AccommodationEntity accommodation = AccommodationEntity.builder()
            .id(100L)
            .build()
            ;

        UserLikeEntity userLikeEntity = UserLikeEntity.builder()
            .id(new UserLikeId(1L, 100L))
            .user(user)
            .accommodation(accommodation)
            .build()
            ;

        UserLikeResponseDto responseDto = UserLikeResponseDto.toDto(userLikeEntity);

        assertEquals(user.getId(), responseDto.getUserId());
        assertEquals(accommodation.getId(), responseDto.getAccommodationId());
    }
}