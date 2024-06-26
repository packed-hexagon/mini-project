package com.group6.accommodation.domain.reservation.converter;

import com.group6.accommodation.domain.reservation.model.dto.ReserveListItemDto;
import com.group6.accommodation.domain.reservation.model.dto.ReserveResponseDto;
import com.group6.accommodation.domain.reservation.model.entity.ReservationEntity;
import org.springframework.stereotype.Component;

@Component
public class ReservationConverter {
    public ReserveListItemDto reserveListItemToDto(ReservationEntity reservation) {
        return ReserveListItemDto.builder()
            .id(reservation.getReservationId())
            .accommodationTitle(reservation.getAccommodation().getTitle())
            .roomTitle(reservation.getRoom().getRoomTitle())
            .thumbnail(reservation.getAccommodation().getThumbnail())
            .startDate(reservation.getStartDate())
            .endDate(reservation.getEndDate())
            .price(reservation.getPrice())
            .createdAt(reservation.getCreatedAt())
            .deletedAt(reservation.getDeletedAt())
            .build();

    }

    public ReserveResponseDto toDto(ReservationEntity reservation) {
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
