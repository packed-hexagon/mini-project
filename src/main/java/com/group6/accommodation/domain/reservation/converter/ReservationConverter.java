package com.group6.accommodation.domain.reservation.converter;

import com.group6.accommodation.domain.reservation.model.dto.ReserveListItemDto;
import com.group6.accommodation.domain.reservation.model.entity.ReservationEntity;
import org.springframework.stereotype.Component;

@Component
public class ReservationConverter {
    public ReserveListItemDto toDto(ReservationEntity reservation) {
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
}
