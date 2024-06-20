package com.group6.accommodation.domain.reservation.service;

import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.accommodation.repository.AccommodationRepository;
import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import com.group6.accommodation.domain.auth.repository.UserRepository;
import com.group6.accommodation.domain.reservation.model.dto.PostReserveRequestDto;
import com.group6.accommodation.domain.reservation.model.dto.PostReserveResponseDto;
import com.group6.accommodation.domain.reservation.model.entity.ReservationEntity;
import com.group6.accommodation.domain.reservation.repository.ReservationRepository;
import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import com.group6.accommodation.domain.room.repository.RoomRepository;
import com.group6.accommodation.global.exception.error.ReservationErrorCode;
import com.group6.accommodation.global.exception.type.ReservationException;
import com.group6.accommodation.global.util.ResponseApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReserveService {

    private final ReservationRepository reservationRepository;
    private final AccommodationRepository accommodationRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseApi<PostReserveResponseDto> postReserve(Long accommodationId, Long roomId,
        PostReserveRequestDto postReserveRequestDto) {
        // 임시 유저 아이디
        Long userId = 1L;

        // 해당 유저가 존재하는 확인
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new ReservationException(ReservationErrorCode.NOT_FOUND_USER));

        // 숙소가 있는지 검증
        AccommodationEntity accommodation = accommodationRepository.findById(accommodationId)
            .orElseThrow(() -> new ReservationException(
                ReservationErrorCode.NOT_FOUND_ACCOMMODATION));

        // 방이 있는지 검증
        RoomEntity room = roomRepository.findById(roomId)
            .orElseThrow(() -> new ReservationException(ReservationErrorCode.NOT_FOUND_ROOM));
        
        // 이미 예약되어 있는 객실인지 검증
        if(reservationRepository.existsByAccommodationAndRoomAndDeletedAtNotNull(accommodation, room)) {
            throw new ReservationException(ReservationErrorCode.ALREADY_RESERVED);
        }

        ReservationEntity reservation = reservationRepository.save(ReservationEntity.builder()
            .accommodation(accommodation)
            .room(room)
            .user(user)
            .headcount(postReserveRequestDto.getHeadcount())
            .startDate(postReserveRequestDto.getStartDate())
            .endDate(postReserveRequestDto.getEndDate())
            .price(postReserveRequestDto.getPrice())
            .build());

        PostReserveResponseDto result = PostReserveResponseDto.builder()
            .userId(userId)
            .id(reservation.getReservationId())
            .price(reservation.getPrice())
            .accommodationId(reservation.getReservationId())
            .headcount(reservation.getHeadcount())
            .startDate(reservation.getStartDate())
            .endDate(reservation.getEndDate())
            .createdAt(reservation.getCreatedAt())
            .build();


        return ResponseApi.success(HttpStatus.CREATED, result);
    }
}
