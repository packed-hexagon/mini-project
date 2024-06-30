package com.group6.accommodation.domain.likes.model.entity;

import static org.junit.jupiter.api.Assertions.*;

import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import org.junit.jupiter.api.Test;

public class UserLikeEntityTest {

    @Test
    public void testBuilder() {
        UserEntity user = new UserEntity();
        user.setId(1L);

        AccommodationEntity accommodation = new AccommodationEntity();
        accommodation.setId(100L);

        UserLikeId userLikeId = new UserLikeId(1L, 100L);

        UserLikeEntity userLikeEntity = UserLikeEntity.builder()
            .id(userLikeId)
            .user(user)
            .accommodation(accommodation)
            .build()
            ;

        assertNotNull(userLikeEntity);
        assertEquals(userLikeId, userLikeEntity.getId());
        assertEquals(user, userLikeEntity.getUser());
        assertEquals(accommodation, userLikeEntity.getAccommodation());
    }
}