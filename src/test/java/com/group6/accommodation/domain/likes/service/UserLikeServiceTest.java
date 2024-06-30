package com.group6.accommodation.domain.likes.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.group6.accommodation.domain.accommodation.converter.AccommodationConverter;
import com.group6.accommodation.domain.accommodation.model.dto.AccommodationResponseDto;
import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.accommodation.repository.AccommodationRepository;
import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import com.group6.accommodation.domain.auth.repository.UserRepository;
import com.group6.accommodation.domain.likes.model.dto.UserLikeResponseDto;
import com.group6.accommodation.domain.likes.model.entity.UserLikeEntity;
import com.group6.accommodation.domain.likes.model.entity.UserLikeId;
import com.group6.accommodation.domain.likes.repository.UserLikeRepository;
import com.group6.accommodation.global.exception.error.UserLikeErrorCode;
import com.group6.accommodation.global.exception.type.UserLikeException;
import com.group6.accommodation.global.model.dto.PagedDto;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;


public class UserLikeServiceTest {

    @InjectMocks
    private UserLikeService userLikeService;

    @Mock
    private UserLikeRepository userLikeRepository;

    @Mock
    private AccommodationRepository accommodationRepository;

    @Mock
    private AccommodationConverter accommodationConverter;

    @Mock
    private UserRepository userRepository;

    private UserEntity user;
    private AccommodationEntity accommodation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = UserEntity.builder()
            .id(1L)
            .email("test@example.com")
            .encryptedPassword("password")
            .phoneNumber("123-456-7890")
            .name("test")
            .build()
            ;

        accommodation = AccommodationEntity.builder()
            .id(1L)
            .title("test")
            .address("123")
            .latitude(1.234)
            .longitude(1.234)
            .build()
            ;
    }

    @Test
    void addLike() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(accommodationRepository.findById(anyLong())).thenReturn(Optional.of(accommodation));
        when(userLikeRepository.findByAccommodationIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.empty());

        UserLikeResponseDto responseDto = userLikeService.addLike(accommodation.getId(), user.getId());

        verify(userLikeRepository).save(any(UserLikeEntity.class));
        verify(accommodationRepository).incrementLikeCount(accommodation.getId());

        assertNotNull(responseDto);
        assertEquals(accommodation.getId(), responseDto.getAccommodationId());
        assertEquals(user.getId(), responseDto.getUserId());
    }

    @Test
    void cancelLike() {
        UserLikeEntity userLikeEntity = UserLikeEntity.builder()
            .id(new UserLikeId(user.getId(), accommodation.getId()))
            .accommodation(accommodation)
            .user(user)
            .build()
            ;

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(accommodationRepository.findById(anyLong())).thenReturn(Optional.of(accommodation));
        when(userLikeRepository.findByAccommodationIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(userLikeEntity));

        String response = userLikeService.cancelLike(accommodation.getId(), user.getId());

        verify(userLikeRepository).delete(any(UserLikeEntity.class));
        verify(accommodationRepository).decrementLikeCount(accommodation.getId());

        assertEquals("Delete Success", response);
    }

    @Test
    void getLikedAccommodation() {
        UserLikeEntity userLikeEntity = UserLikeEntity.builder()
            .id(new UserLikeId(user.getId(), accommodation.getId()))
            .accommodation(accommodation)
            .user(user)
            .build()
            ;

        List<UserLikeEntity> userLikeEntities = Collections.singletonList(userLikeEntity);
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<AccommodationEntity> accommodationEntities = Collections.singletonList(accommodation);
        Page<AccommodationEntity> accommodationEntityPage = new PageImpl<>(accommodationEntities, pageRequest, accommodationEntities.size());
        AccommodationResponseDto accommodationResponseDto = new AccommodationResponseDto();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userLikeRepository.findByUserId(anyLong())).thenReturn(userLikeEntities);
        when(accommodationRepository.findByIdIn(anyList(), any(PageRequest.class))).thenReturn(accommodationEntityPage);
        when(accommodationConverter.toDtoList(anyList())).thenReturn(Collections.singletonList(accommodationResponseDto));

        PagedDto<AccommodationResponseDto> response = userLikeService.getLikedAccommodation(user.getId(), 0, 10);

        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        assertEquals(1, response.getContent().size());
        assertEquals(accommodationResponseDto, response.getContent().get(0));
    }

    @Test
    void addLikeAlreadyExists() {
        UserLikeEntity testLike = UserLikeEntity.builder()
                .id(new UserLikeId(user.getId(), accommodation.getId()))
                .accommodation(accommodation)
                .user(user)
                .build()
                ;

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(accommodationRepository.findById(anyLong())).thenReturn(Optional.of(accommodation));
        when(userLikeRepository.findByAccommodationIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(testLike));

        UserLikeException exception = assertThrows(UserLikeException.class, () -> {
            userLikeService.addLike(accommodation.getId(), user.getId());
        });

        assertEquals(UserLikeErrorCode.ALREADY_ADD_LIKE.getCode(), exception.getStatusCode());
        assertEquals(UserLikeErrorCode.ALREADY_ADD_LIKE.getInfo(), exception.getInfo());
    }

    @Test
    void cancelLikeNotExists() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(accommodationRepository.findById(anyLong())).thenReturn(Optional.of(accommodation));
        when(userLikeRepository.findByAccommodationIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.empty());

        UserLikeException exception = assertThrows(UserLikeException.class, () -> {
            userLikeService.cancelLike(accommodation.getId(), user.getId());
        });

        assertEquals(UserLikeErrorCode.ACCOMMODATION_NOT_LIKED.getCode(), exception.getStatusCode());
        assertEquals(UserLikeErrorCode.ACCOMMODATION_NOT_LIKED.getInfo(), exception.getInfo());
    }
}