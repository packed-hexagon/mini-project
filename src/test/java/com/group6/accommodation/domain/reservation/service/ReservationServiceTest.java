package com.group6.accommodation.domain.reservation.service;

import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import com.group6.accommodation.domain.auth.repository.UserRepository;
import com.group6.accommodation.domain.reservation.model.dto.PostReservationRequestDto;
import com.group6.accommodation.domain.reservation.model.dto.ReserveListItemDto;
import com.group6.accommodation.domain.reservation.model.dto.ReservationResponseDto;
import com.group6.accommodation.domain.reservation.model.entity.ReservationEntity;
import com.group6.accommodation.domain.reservation.repository.ReservationRepository;
import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import com.group6.accommodation.domain.room.repository.RoomRepository;
import com.group6.accommodation.global.exception.error.ReservationErrorCode;
import com.group6.accommodation.global.exception.type.ReservationException;
import com.group6.accommodation.global.model.dto.PagedDto;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
public class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserRepository userRepository;


    private ReservationEntity reservation;
    private AccommodationEntity accommodation;
    private RoomEntity room;

    @BeforeEach
    void setUp() {
        accommodation = AccommodationEntity.builder()
                .title("Sample Accommodation")
                .address("123 Main St")
                .areacode("12345")
                .category("Hotel")
                .image("sample_image.jpg")
                .thumbnail("sample_thumbnail.jpg")
                .latitude(37.123456)
                .longitude(-122.654321)
                .tel("123-456-7890")
                .likeCount(100)
                .reviewCount(50)
                .totalRating(4.5)
                .build();

        room = RoomEntity.builder()
                .accommodation(accommodation)
                .count(2)
                .baseCount(2)
                .maxHeadCount(4)
                .weekdaysFee(10000)
                .weekendsFee(20000)
                .images("image1.png,image2.png")
                .checkInTime(LocalDate.now().plusDays(1))
                .checkOutTime(LocalDate.now().plusDays(2))
                .build();

        reservation = ReservationEntity.builder()
                .user(mock(UserEntity.class))
                .room(room)
                .headcount(2)
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(2))
                .price(10000)
                .build();
    }

    @Test
    @DisplayName("예약 하기 - 성공")
    public void reserveSuccess() {
        // given
        Long userId = 1L;
        Long accommodationId = accommodation.getId();
        Long roomId = room.getRoomId();
        PostReservationRequestDto requestDto = new PostReservationRequestDto(
                2,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2)
        );

        when(roomRepository.getById(roomId)).thenReturn(room);
        when(userRepository.getById(userId)).thenReturn(mock(UserEntity.class));
        when(reservationRepository.save(any(ReservationEntity.class))).thenReturn(reservation);

        // when
        ReservationResponseDto result = reservationService.createReservation(userId, roomId, requestDto);

        // then
        assertNotNull(result);
        assertEquals(1, room.getCount());
        assertEquals(accommodationId, result.getAccommodationId());
        assertEquals(roomId, result.getRoomId());
    }

    @Test
    @Disabled
    @DisplayName("예약하기 - 인원 수가 초과 되는 경우")
    public void overPeopleReserve() {
        // given
        Long userId = 1L;
        Long accommodationId = accommodation.getId();
        Long roomId = room.getRoomId();
        int overPeople = 5;

        PostReservationRequestDto requestDto = new PostReservationRequestDto(
            overPeople,
            LocalDate.now().plusDays(1),
            LocalDate.now().plusDays(2),
            150100
        );

        when(accommodationRepository.findById(accommodationId)).thenReturn(Optional.of(accommodation));
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(userRepository.findById(userId)).thenReturn(Optional.of(mock(UserEntity.class)));

        // when
        ReservationException exception = assertThrows(ReservationException.class,
            () -> reserveService.createReservation(userId, accommodationId, roomId, requestDto));

        // then
        assertNotNull(exception);
        assertEquals(ReservationErrorCode.OVER_PEOPLE.getInfo(), exception.getInfo());
    }

    @Test
    @Disabled
    @DisplayName("예약하기 - 금액이 맞지 않는 경우")
    public void notMatchPriceReserve() {
        Long userId = 1L;
        Long accommodationId = accommodation.getId();
        Long roomId = room.getRoomId();
        int overPrice = 1000;

        PostReservationRequestDto requestDto = new PostReservationRequestDto(
            2,
            LocalDate.now().plusDays(1),
            LocalDate.now().plusDays(2),
            overPrice
        );

        when(accommodationRepository.findById(accommodationId)).thenReturn(Optional.of(accommodation));
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(userRepository.findById(userId)).thenReturn(Optional.of(mock(UserEntity.class)));

        // when
        ReservationException exception = assertThrows(ReservationException.class,
            () -> reserveService.createReservation(userId, accommodationId, roomId, requestDto));

        // then
        assertEquals(exception.getInfo(), ReservationErrorCode.NOT_MATCH_PRICE.getInfo());
    }



    @Test
    @Disabled
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
                    .headcount(0)
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
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));

        // when
        ReservationResponseDto result = reserveService.cancelReserve(reservationId);

        // then
        assertNotNull(result);
        assertNotNull(reservation.getDeletedAt());
    }





    @Test
    @DisplayName("에약 취소 - 이미 취소한 예약")
    public void cancelAlreadyReserve() {

        // given
        Long reservationId = 1L;
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));
        reservation.setDeletedAt(Instant.now());

        // when

        ReservationException exception = assertThrows(ReservationException.class,
            () -> reserveService.cancelReserve(reservationId));

        // then
        assertEquals(ReservationErrorCode.ALREADY_CANCEL.getInfo(), exception.getInfo());


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
    }
}
