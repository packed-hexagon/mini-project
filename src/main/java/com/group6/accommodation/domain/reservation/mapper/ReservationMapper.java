package com.group6.accommodation.domain.reservation.mapper;

import com.group6.accommodation.domain.reservation.model.dto.ReservationResponseDto;
import com.group6.accommodation.domain.reservation.model.entity.ReservationEntity;

public class ReservationMapper {
    public static ReservationResponseDto toDto(ReservationEntity reservation) {
        return ReservationResponseDto.builder()
                .id(reservation.getReservationId())
                .userId(reservation.getUser().getId())
                .roomId(reservation.getRoom().getRoomId())
                .headcount(reservation.getHeadcount())
                .price(reservation.getPrice())
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .build();
    }
}
