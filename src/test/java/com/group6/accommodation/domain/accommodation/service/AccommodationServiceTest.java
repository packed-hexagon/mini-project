package com.group6.accommodation.domain.accommodation.service;

import com.group6.accommodation.domain.accommodation.converter.AccommodationConverter;
import com.group6.accommodation.domain.accommodation.model.dto.AccommodationResponseDto;
import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.accommodation.repository.AccommodationRepository;
import com.group6.accommodation.global.model.dto.PagedDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class AccommodationServiceTest {

    @Mock
    private AccommodationRepository accommodationRepository;

    @Mock
    private AccommodationConverter accommodationConverter;
    

    @InjectMocks
    private AccommodationService accommodationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByParameter() {
    }

    @Test
    @DisplayName("숙소 전체 조회 테스트")
    void findAllPage() {
        // Given
        int page = 0;
        int size = 12;
        List<AccommodationEntity> accommodations = Arrays.asList(new AccommodationEntity(), new AccommodationEntity());
        Page<AccommodationEntity> accommodationPage = new PageImpl<>(accommodations, PageRequest.of(page, size), accommodations.size());

        when(accommodationRepository.findAll(any(Pageable.class))).thenReturn(accommodationPage);
        when(accommodationConverter.toDtoList(accommodations)).thenReturn(Arrays.asList(new AccommodationResponseDto(), new AccommodationResponseDto()));

        // When
        PagedDto<AccommodationResponseDto> result = accommodationService.findAllPage(page, size);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(12, result.getSize());
        assertEquals(0, result.getCurrentPage());
        assertEquals(2, result.getContent().size());

        verify(accommodationRepository).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("종류별 숙소 조회")
    void findByCategoryPaged() {
        // Given
        String category = "호텔";
        int page = 0;
        int size = 9;

        List<AccommodationEntity> accommodations = Arrays.asList(new AccommodationEntity(), new AccommodationEntity());
        Page<AccommodationEntity> accommodationPage = new PageImpl<>(accommodations, PageRequest.of(page, size), accommodations.size());

        when(accommodationRepository.findByCategory(any(), any(Pageable.class))).thenReturn(accommodationPage);
        when(accommodationConverter.toDtoList(accommodations)).thenReturn(Arrays.asList(new AccommodationResponseDto(), new AccommodationResponseDto()));

        // When
        PagedDto<AccommodationResponseDto> result = accommodationService.findByCategoryPaged(category, page, size);

        // Then
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(9, result.getSize());
        assertEquals(0, result.getCurrentPage());
        assertEquals(2, result.getContent().size());
    }

    @Test
    @DisplayName("위치별 숙소 조회")
    void findByAreaPaged() {
        // Given
        String area = "서울";
        int page = 0;
        int size = 9;

        List<AccommodationEntity> accommodations = Arrays.asList(new AccommodationEntity(), new AccommodationEntity());
        Page<AccommodationEntity> accommodationPage = new PageImpl<>(accommodations, PageRequest.of(page, size), accommodations.size());

        when(accommodationRepository.findByAreacode(any(), any(Pageable.class))).thenReturn(accommodationPage);
        when(accommodationConverter.toDtoList(accommodations)).thenReturn(Arrays.asList(new AccommodationResponseDto(), new AccommodationResponseDto()));

        // When
        PagedDto<AccommodationResponseDto> result = accommodationService.findByAreaPaged(area, page, size);

        // Then
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(9, result.getSize());
        assertEquals(0, result.getCurrentPage());
        assertEquals(2, result.getContent().size());
    }

    @Test
    void findById() {
    }

    @Test
    @DisplayName("키워드별 숙소 조회")
    void findByKeywordPaged() {
        // Given
        String keyword = "강";
        int page = 0;
        int size = 9;

        List<AccommodationEntity> accommodations = Arrays.asList(new AccommodationEntity(), new AccommodationEntity());
        Page<AccommodationEntity> accommodationPage = new PageImpl<>(accommodations, PageRequest.of(page, size), accommodations.size());

        when(accommodationRepository.findByTitleOrAddressContainingKeyword(any(), any(Pageable.class))).thenReturn(accommodationPage);
        when(accommodationConverter.toDtoList(accommodations)).thenReturn(Arrays.asList(new AccommodationResponseDto(), new AccommodationResponseDto()));

        // When
        PagedDto<AccommodationResponseDto> result = accommodationService.findByKeywordPaged(keyword, page);

        // Then
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(9, result.getSize());
        assertEquals(0, result.getCurrentPage());
        assertEquals(2, result.getContent().size());
    }
}