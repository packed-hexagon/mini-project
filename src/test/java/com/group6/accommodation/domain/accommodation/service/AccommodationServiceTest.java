package com.group6.accommodation.domain.accommodation.service;

import com.group6.accommodation.domain.accommodation.converter.AccommodationConverter;
import com.group6.accommodation.domain.accommodation.model.dto.AccommodationDetailResponseDto;
import com.group6.accommodation.domain.accommodation.model.dto.AccommodationResponseDto;
import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.accommodation.repository.AccommodationRepository;
import com.group6.accommodation.global.exception.type.AccommodationException;
import com.group6.accommodation.global.model.dto.PagedDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        int size = 12;

        List<AccommodationEntity> accommodations = Arrays.asList(new AccommodationEntity(), new AccommodationEntity());
        Page<AccommodationEntity> accommodationPage = new PageImpl<>(accommodations, PageRequest.of(page, size), accommodations.size());

        when(accommodationRepository.findByCategory(any(), any(Pageable.class))).thenReturn(accommodationPage);
        when(accommodationConverter.toDtoList(accommodations)).thenReturn(Arrays.asList(new AccommodationResponseDto(), new AccommodationResponseDto()));

        // When
        PagedDto<AccommodationResponseDto> result = accommodationService.findByCategoryPaged(category, page, size);

        // Then
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(12, result.getSize());
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
    @DisplayName("숙소ID로 숙소 조회 - 존재하는 경우")
    void findById() {
        // Given
        Long id = 1L;
        AccommodationEntity accommodation = AccommodationEntity.builder()
                .id(id)
                .build();
        when(accommodationRepository.findById(id)).thenReturn(Optional.of(accommodation));
        when(accommodationConverter.toDetailDto(accommodation)).thenReturn(new AccommodationDetailResponseDto());

        // When
        AccommodationDetailResponseDto result = accommodationService.findById(id);

        // Then
        assertNotNull(result);
        verify(accommodationRepository).findById(id);
        verify(accommodationConverter).toDetailDto(accommodation);
    }

    @Test
    @DisplayName("숙소 ID로 조회 - 존재하지 않는 경우")
    void findByIdNotExisting() {
        // Given
        Long id = 1L;
        when(accommodationRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(AccommodationException.class, () -> accommodationService.findById(id));
        verify(accommodationRepository).findById(id);
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

    @Test
    @DisplayName("키워드별 숙소 조회 - 결과 없음")
    void findByKeywordNull() {
        // Given
        String keyword = "존재하지않는키워드";
        int page = 0;
        when(accommodationRepository.findByTitleOrAddressContainingKeyword(any(), any(Pageable.class))).thenReturn(Page.empty());

        // When & Then
        assertThrows(AccommodationException.class, () -> accommodationService.findByKeywordPaged(keyword, page));
    }

    @Test
    @DisplayName("조건으로 숙소 조회")
    void findByAvaliableCondition() {
        // Given
        String area = "서울";
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(3);
        Integer headcount = 2;
        int page = 0;

        List<AccommodationEntity> accommodations = Arrays.asList(new AccommodationEntity(), new AccommodationEntity());
        Page<AccommodationEntity> accommodationPage = new PageImpl<>(accommodations, PageRequest.of(page, 9), accommodations.size());

        when(accommodationRepository.findAll(any(Specification.class))).thenReturn(accommodations);
        when(accommodationRepository.findAllWithCountQuery(anyList(), any(Pageable.class))).thenReturn(accommodationPage);
        when(accommodationConverter.toDtoList(accommodations)).thenReturn(Arrays.asList(new AccommodationResponseDto(), new AccommodationResponseDto()));

        // When
        PagedDto<AccommodationResponseDto> result = accommodationService.findAvaliableAccommodation(area, startDate, endDate, headcount, page);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(9, result.getSize());
        assertEquals(0, result.getCurrentPage());
        assertEquals(2, result.getContent().size());
        verify(accommodationRepository).findAll(any(Specification.class));
        verify(accommodationRepository).findAllWithCountQuery(anyList(), any(Pageable.class));
    }
}