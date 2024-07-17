package com.group6.accommodation.domain.reservation.service;

import com.group6.accommodation.domain.reservation.model.dto.PostReservationRequestDto;
import com.group6.accommodation.domain.reservation.model.dto.ReservationResponseDto;
import com.group6.accommodation.domain.accommodation.repository.AccommodationRepository;
import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import com.group6.accommodation.domain.auth.repository.UserRepository;
import com.group6.accommodation.domain.reservation.model.entity.ReservationEntity;
import com.group6.accommodation.domain.reservation.repository.ReservationRepository;
import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import com.group6.accommodation.domain.room.repository.RoomRepository;
import com.group6.accommodation.global.exception.error.ReservationErrorCode;
import com.group6.accommodation.global.exception.type.ReservationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final AccommodationRepository accommodationRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    private final int OVER_PRICE = 50000;

    @Transactional
    public ReservationResponseDto postReservation(Long userId, Long roomId, PostReservationRequestDto requestDto) {
        UserEntity user = userRepository.getById(userId);
        RoomEntity room = roomRepository.getById(roomId);

        // 가격 구하기
        int price = room.getPrice(requestDto, OVER_PRICE);

        // 인원수 검증
        if(room.isOverHeadcount(requestDto.getHeadcount())) {
            throw new ReservationException(ReservationErrorCode.OVER_HEADCOUNT);
        }

        // 방 예약
        if(!room.reserve()) {
            throw new ReservationException(ReservationErrorCode.FULL_ROOM);
        }

        ReservationEntity reservationEntity = ReservationEntity.builder()
            .room(room)
            .user(user)
            .headcount(requestDto.getHeadcount())
            .startDate(requestDto.getStartDate())
            .endDate(requestDto.getEndDate())
            .price(price)
            .build();
        reservationRepository.save(reservationEntity);

        return ReservationEntity.toDto(reservationEntity);
    }


    // TODO: 임시 주석처리

//    @Transactional
//    public ReserveResponseDto cancelReserve(Long reservationId) {
//        ReservationEntity reservation = reservationRepository.findById(reservationId).orElseThrow(
//            () -> new ReservationException(ReservationErrorCode.NOT_FOUND_RESERVATION));
//
//        // 이미 취소된 예약
//        if(reservation.getDeletedAt() != null) {
//            throw new ReservationException(ReservationErrorCode.ALREADY_CANCEL);
//        }
//
//        // 예약 취소
////        reservation.setDeletedAt(Instant.now());
//
//        return ReservationConverter.toDto(reservation);
//    }

    // TODO: 임시 주석처리

//    @Transactional(readOnly = true)
//    public PagedDto<ReserveListItemDto> getList(Long userId, int page, int size, String directionStr) {
//        Sort.Direction direction =
//            directionStr.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
//
//        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt"));
//        Page<ReserveListItemDto> result = reservationRepository.findAllByUserId(userId,
//            pageable).map(item -> ReserveListItemDto.builder()
//            .id(item.getReservationId())
//            .accommodationTitle(item.getAccommodation().getTitle())
//            .roomTitle(item.getRoom().getTitle())
//            .thumbnail(item.getAccommodation().getThumbnail())
//            .startDate(item.getStartDate())
//            .endDate(item.getEndDate())
//            .price(item.getPrice())
//            .createdAt(item.getCreatedAt())
//            .deletedAt(item.getDeletedAt())
//            .build());
//
//
//        return PagedDto.<ReserveListItemDto>builder()
//            .totalElements((int) result.getTotalElements())
//            .totalPages(result.getTotalPages())
//            .size(result.getSize())
//            .currentPage(result.getNumber())
//            .content(result.getContent())
//            .build();
//
//    }
}
