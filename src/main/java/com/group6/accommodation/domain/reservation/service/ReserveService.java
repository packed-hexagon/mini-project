package com.group6.accommodation.domain.reservation.service;

import com.group6.accommodation.domain.reservation.mapper.ReservationMapper;
import com.group6.accommodation.global.model.dto.PagedDto;
import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import com.group6.accommodation.domain.auth.repository.UserRepository;
import com.group6.accommodation.domain.reservation.model.dto.PostReserveRequestDto;
import com.group6.accommodation.domain.reservation.model.dto.ReserveListItemDto;
import com.group6.accommodation.domain.reservation.model.dto.ReservationResponseDto;
import com.group6.accommodation.domain.reservation.model.entity.ReservationEntity;
import com.group6.accommodation.domain.reservation.repository.ReservationRepository;
import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import com.group6.accommodation.domain.room.repository.RoomRepository;
import com.group6.accommodation.global.exception.error.ReservationErrorCode;
import com.group6.accommodation.global.exception.type.ReservationException;
import java.time.Instant;
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
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    private final int OVER_PRICE = 50000;

    @Transactional
    public ReservationResponseDto createReservation(Long userId, Long roomId, PostReserveRequestDto postReserveRequestDto) {
        UserEntity user = userRepository.getById(userId);
        RoomEntity room = roomRepository.getById(roomId);

        // 모든 방이 예약이 된 경우
        if(room.decrease() > 0) {
            throw new ReservationException(ReservationErrorCode.FULL_ROOM);
        }

        int price = room.getPayment(postReserveRequestDto, OVER_PRICE);


        ReservationEntity reservationEntity = ReservationEntity.builder()
            .room(room)
            .user(user)
            .headcount(postReserveRequestDto.getHeadcount())
            .startDate(postReserveRequestDto.getStartDate())
            .endDate(postReserveRequestDto.getEndDate())
            .price(price)
            .build();


        ReservationEntity reservation = reservationRepository.save(reservationEntity);
        return ReservationMapper.toDto(reservation);
    }



    @Transactional
    public ReservationResponseDto cancelReserve(Long reservationId) {
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
            .roomTitle(item.getRoom().getTitle())
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



}
