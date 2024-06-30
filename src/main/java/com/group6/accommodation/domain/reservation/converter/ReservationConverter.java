package com.group6.accommodation.domain.reservation.converter;

import com.group6.accommodation.domain.reservation.model.dto.ReserveResponseDto;
import com.group6.accommodation.domain.reservation.model.entity.ReservationEntity;


public class ReservationConverter {

    public static ReserveResponseDto toDto(ReservationEntity reservation) {
        return ReserveResponseDto.builder()
            .id(reservation.getReservationId())
            .userId(reservation.getUser().getId())
            .accommodationId(reservation.getAccommodation().getId())
            .price(reservation.getPrice())
            .roomId(reservation.getRoom().getRoomId())
            .headcount(reservation.getHeadcount())
            .startDate(reservation.getStartDate())
            .endDate(reservation.getEndDate())
            .createdAt(reservation.getCreatedAt())
            .deletedAt(reservation.getDeletedAt())
            .build();
    }

}
