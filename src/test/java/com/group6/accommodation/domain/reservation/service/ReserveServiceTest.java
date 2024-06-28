package com.group6.accommodation.domain.reservation.service;

import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import com.group6.accommodation.domain.reservation.model.dto.ReserveListItemDto;
import com.group6.accommodation.domain.reservation.model.dto.ReserveResponseDto;
import com.group6.accommodation.domain.reservation.model.entity.ReservationEntity;
import com.group6.accommodation.domain.reservation.repository.ReservationRepository;
import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import com.group6.accommodation.global.exception.error.ReservationErrorCode;
import com.group6.accommodation.global.exception.type.ReservationException;
import com.group6.accommodation.global.model.dto.PagedDto;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReserveServiceTest {

    @InjectMocks
    private ReserveService reserveService;

    @Mock
    private ReservationRepository reservationRepository;

    private ReservationEntity reservation;
    private RoomEntity room;

    @BeforeEach
    void setUp() {
        room = mock(RoomEntity.class);
        reservation = ReservationEntity.builder()
            .user(mock(UserEntity.class))
            .room(room)
            .accommodation(mock(AccommodationEntity.class))
            .headcount(2)
            .startDate(LocalDate.now().plusDays(1))
            .endDate(LocalDate.now().plusDays(2))
            .price(1000)
            .build();
    }


    @Test
    @DisplayName("모든 예약 정보 가져오기 - 성공")
    void getList() {
        // Given
        final int count = 15;
        int size = 5;
        long userId = 1L;
        int totalPage = (count + size - 1) / size;
        String direction = "asc";

        List<ReserveListItemDto> dataList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            dataList.add(ReserveListItemDto.builder()
                .id((long) i)
                .createdAt(Instant.now())
                .accommodationTitle("테스트" + i)
                .thumbnail("image" + i + ".jpg")
                .price(1000 * i)
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(2))
                .build());
        }

        for (int i = 0; i < totalPage; i++) {
            int start = i * size;
            int end = Math.min(start + size, count);
            List<ReserveListItemDto> subList = dataList.subList(start, end);

            List<ReservationEntity> reservationEntities = subList.stream().map(dto ->
                ReservationEntity.builder()
                    .user(mock(UserEntity.class))
                    .room(mock(RoomEntity.class))
                    .accommodation(mock(AccommodationEntity.class))
                    .headcount(2)
                    .startDate(dto.getStartDate())
                    .endDate(dto.getEndDate())
                    .price(dto.getPrice())
                    .build()).toList();

            Page<ReservationEntity> page = new PageImpl<>(reservationEntities,
                PageRequest.of(i, size, Sort.by(Sort.Direction.ASC, "createdAt")), count);

            when(reservationRepository.findAllByUserId(userId,
                PageRequest.of(i, size, Sort.by(Sort.Direction.ASC, "createdAt"))))
                .thenReturn(page);

            // when
            PagedDto<ReserveListItemDto> list = reserveService.getList(userId, i, size, direction);

            // Then
            assertNotNull(list);
            assertEquals(subList.size(), list.getContent().size());
            assertEquals(i, list.getCurrentPage());
            assertEquals(totalPage, list.getTotalPages());
            assertEquals(count, list.getTotalElements());
            assertEquals(size, list.getSize());
        }

        verify(reservationRepository, times(totalPage)).findAllByUserId(eq(userId),
            any(Pageable.class));
    }


    @Test
    @DisplayName("예약 취소 - 성공")
    public void cancelSuccessReservation() {

        // given
        Long reservationId = 1L;
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        // when
        ReserveResponseDto result = reserveService.cancelReserve(reservationId);

        // then
        assertNotNull(result);
        assertNotNull(reservation.getDeletedAt());

        verify(room, times(1)).increment();
        verify(reservationRepository, times(1)).findById(reservationId);
    }


    @Test
    @DisplayName("에약 취소 - 이미 취소한 예약")
    public void cancelAlreadyReserve() {

        // given
        Long reservationId = 1L;
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        reservation.setDeletedAt(Instant.now());

        // when

        ReservationException exception = assertThrows(ReservationException.class,
            () -> reserveService.cancelReserve(reservationId));

        // then
        assertEquals(ReservationErrorCode.ALREADY_CANCEL.getInfo(), exception.getInfo());
        verify(room, never()).increment();
        verify(reservationRepository, times(1)).findById(reservationId);
    }

    @Test
    @DisplayName("예약 취소 - 예약 없음")
    void cancelReserveNotFound() {
        Long reservationId = 1L;

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        ReservationException exception = assertThrows(ReservationException.class,
            () -> reserveService.cancelReserve(reservationId));

        assertEquals(ReservationErrorCode.NOT_FOUND_RESERVATION.getInfo(), exception.getInfo());

        verify(reservationRepository, times(1)).findById(reservationId);
        verify(room, never()).increment();
    }
}
