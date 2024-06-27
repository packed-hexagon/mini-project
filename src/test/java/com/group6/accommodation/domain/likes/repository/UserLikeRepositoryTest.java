package com.group6.accommodation.domain.likes.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import com.group6.accommodation.domain.likes.model.entity.UserLikeEntity;
import com.group6.accommodation.domain.likes.model.entity.UserLikeId;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserLikeRepositoryTest {

    @Mock
    private UserLikeRepository userLikeRepository;

    Long accommodationId = 100L;
    Long userId = 1L;

    private UserLikeEntity createUserLikeEntity() {
        UserEntity user = new UserEntity();
        user.setId(userId);

        AccommodationEntity accommodation = new AccommodationEntity();
        accommodation.setId(accommodationId);

        UserLikeId userLikeId = new UserLikeId(userId, accommodationId);

        return UserLikeEntity.builder()
            .id(userLikeId)
            .user(user)
            .accommodation(accommodation)
            .build()
            ;
    }

    @Test
    @DisplayName("숙박 ID와 유저 ID로 찜한 숙박 찾기")
    public void testFindByAccommodationIdAndUserId() {

        UserLikeEntity userLikeEntity = createUserLikeEntity();

    }
}