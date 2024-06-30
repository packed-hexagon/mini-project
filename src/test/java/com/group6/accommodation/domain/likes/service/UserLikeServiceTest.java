package com.group6.accommodation.domain.likes.service;

import static org.junit.jupiter.api.Assertions.*;

import com.group6.accommodation.domain.accommodation.converter.AccommodationConverter;
import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.accommodation.repository.AccommodationRepository;
import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import com.group6.accommodation.domain.likes.repository.UserLikeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class UserLikeServiceTest {

    @InjectMocks
    private UserLikeService userLikeService;

    @Mock
    private UserLikeRepository userLikeRepository;

    @Mock
    private AccommodationRepository accommodationRepository;

    @Mock
    private AccommodationConverter accommodationConverter;

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


    }
}