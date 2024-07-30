package com.group6.accommodation.domain.reservation.service;

import com.group6.accommodation.domain.reservation.model.dto.PostReservationRequestDto;
import com.group6.accommodation.domain.reservation.model.dto.ReservationRequestParamDto;
import com.group6.accommodation.domain.reservation.model.dto.ReservationResponseDto;
import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import com.group6.accommodation.domain.auth.repository.UserRepository;
import com.group6.accommodation.domain.reservation.model.dto.ReservationListItemDto;
import com.group6.accommodation.domain.reservation.model.entity.ReservationEntity;
import com.group6.accommodation.domain.reservation.repository.ReservationRepository;
import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import com.group6.accommodation.domain.room.repository.RoomRepository;
import com.group6.accommodation.global.annotation.RedissonLock;
import com.group6.accommodation.global.exception.error.ReservationErrorCode;
import com.group6.accommodation.global.exception.type.ReservationException;
import com.group6.accommodation.global.model.dto.PagedDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    private final int OVER_PRICE = 50000;

    @RedissonLock(key = "#roomId")
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

        ReservationEntity reservationEntity = ReservationEntity.of(room, user, price, requestDto);

        reservationRepository.save(reservationEntity);

        return ReservationResponseDto.from(reservationEntity);
    }

    @Transactional
    public ReservationResponseDto cancelReserve(Long reservationId) {
        ReservationEntity reservation = reservationRepository.getByIdOrElseThrow(reservationId);
        reservationRepository.delete(reservation);
        return ReservationResponseDto.from(reservation);
    }

    @Transactional(readOnly = true)
    public PagedDto<ReservationListItemDto> getList(Long userId, ReservationRequestParamDto requestParam) {
        Sort.Direction direction = requestParam.getDirection().equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(requestParam.getPage(), requestParam.getSize(), Sort.by(direction, "createdAt"));
        Page<ReservationListItemDto> result = reservationRepository.findAllByUserId(userId, pageable);

        return PagedDto.from(result);
    }
}
