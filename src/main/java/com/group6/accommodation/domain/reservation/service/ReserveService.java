package com.group6.accommodation.domain.reservation.service;

import com.group6.accommodation.global.model.dto.PagedDto;
import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.accommodation.repository.AccommodationRepository;
import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import com.group6.accommodation.domain.auth.repository.UserRepository;
import com.group6.accommodation.domain.reservation.converter.ReservationConverter;
import com.group6.accommodation.domain.reservation.model.dto.PostReserveRequestDto;
import com.group6.accommodation.domain.reservation.model.dto.ReserveListItemDto;
import com.group6.accommodation.domain.reservation.model.dto.ReserveResponseDto;
import com.group6.accommodation.domain.reservation.model.entity.ReservationEntity;
import com.group6.accommodation.domain.reservation.repository.ReservationRepository;
import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import com.group6.accommodation.domain.room.repository.RoomRepository;
import com.group6.accommodation.global.exception.error.ReservationErrorCode;
import com.group6.accommodation.global.exception.type.ReservationException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReserveService {

    private final ReservationRepository reservationRepository;
    private final AccommodationRepository accommodationRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    private final int OVER_PRICE = 50000;

    @Transactional
    public ReserveResponseDto postReserve(Long userId, Long accommodationId, Long roomId,
        PostReserveRequestDto postReserveRequestDto) {

        // 숙소가 있는지 검증
        AccommodationEntity accommodation = accommodationRepository.findById(accommodationId)
            .orElseThrow(() -> new ReservationException(
                ReservationErrorCode.NOT_FOUND_ACCOMMODATION));

        // 유저 정보 가져오기
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new ReservationException(ReservationErrorCode.NOT_FOUND_USER));

        // 방 정보 가져오기
        RoomEntity room = roomRepository.findById(roomId)
            .orElseThrow(() -> new ReservationException(ReservationErrorCode.NOT_FOUND_ROOM));

        // 방이 모두 예약 된 경우
        if (reservationRepository.countByRoom(room) >= room.getRoomCount()) {
            throw new ReservationException(ReservationErrorCode.FULL_ROOM);
        }

        // 금액 검증
        validatePayable(room, postReserveRequestDto);

        ReservationEntity reservationEntity = ReservationEntity.builder()
            .accommodation(accommodation)
            .room(room)
            .user(user)
            .headcount(postReserveRequestDto.getHeadcount())
            .startDate(postReserveRequestDto.getStartDate())
            .endDate(postReserveRequestDto.getEndDate())
            .price(postReserveRequestDto.getPrice())
            .build();

        ReservationEntity reservation = reservationRepository.save(reservationEntity);


        return ReservationConverter.toDto(reservation);
    }



    @Transactional
    public ReserveResponseDto cancelReserve(Long reservationId) {
        ReservationEntity reservation = reservationRepository.findById(reservationId).orElseThrow(
            () -> new ReservationException(ReservationErrorCode.NOT_FOUND_RESERVATION));

        // 이미 취소된 예약
        if(reservation.getDeletedAt() != null) {
            throw new ReservationException(ReservationErrorCode.ALREADY_CANCEL);
        }

        // 예약 취소
        reservation.setDeletedAt(Instant.now());

        return ReservationConverter.toDto(reservation);
    }


    @Transactional(readOnly = true)
    public PagedDto<ReserveListItemDto> getList(Long userId, int page, int size, String directionStr) {
        Sort.Direction direction =
            directionStr.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt"));
        Page<ReserveListItemDto> result = reservationRepository.findAllByUserId(userId,
            pageable).map(item -> ReserveListItemDto.builder()
            .id(item.getReservationId())
            .accommodationTitle(item.getAccommodation().getTitle())
            .roomTitle(item.getRoom().getRoomTitle())
            .thumbnail(item.getAccommodation().getThumbnail())
            .startDate(item.getStartDate())
            .endDate(item.getEndDate())
            .price(item.getPrice())
            .createdAt(item.getCreatedAt())
            .deletedAt(item.getDeletedAt())
            .build());


        return PagedDto.<ReserveListItemDto>builder()
            .totalElements((int) result.getTotalElements())
            .totalPages(result.getTotalPages())
            .size(result.getSize())
            .currentPage(result.getNumber())
            .content(result.getContent())
            .build();

    }


    private void validatePayable(RoomEntity room, PostReserveRequestDto postReserveRequestDto) {

        int headCount = postReserveRequestDto.getHeadcount();
        int amount = postReserveRequestDto.getPrice();
        int price = room.getRoomOffseasonMinfee1();

        int day = (int) ChronoUnit.DAYS.between(postReserveRequestDto.getStartDate(),
            postReserveRequestDto.getEndDate());

        int overCount = headCount - room.getRoomBaseCount();
        for(int i = 0; i < overCount; i++) {
            price += OVER_PRICE;
        }
        price *= day;

        if(price != amount) {
            throw new ReservationException(ReservationErrorCode.NOT_MATCH_PRICE);
        }
    }
}
