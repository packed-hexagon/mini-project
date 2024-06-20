package com.group6.accommodation.domain.reservation.model.dto;

import java.time.Instant;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PostReserveResponseDto {
    private Long id;
    private Long userId;
    private Long accommodationId;
    private Integer price;
    private Integer headcount;
    private LocalDate startDate;
    private LocalDate endDate;
    private Instant createdAt;
}
