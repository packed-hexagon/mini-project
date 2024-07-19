package com.group6.accommodation.domain.reservation.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.group6.accommodation.domain.reservation.model.entity.ReservationEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ReservationResponseDto {
    private Long id;
    private Long userId;
    private Long accommodationId;
    private Long roomId;
    private Integer price;
    private Integer headcount;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    public static ReservationResponseDto from(ReservationEntity entity) {
        return ReservationResponseDto.builder()
            .id(entity.getId())
            .userId(entity.getUser().getId())
            .headcount(entity.getHeadcount())
            .price(entity.getPrice())
            .startDate(entity.getStartDate())
            .endDate(entity.getEndDate())
            .deletedAt(entity.getDeletedAt())
            .build();
    }



}
