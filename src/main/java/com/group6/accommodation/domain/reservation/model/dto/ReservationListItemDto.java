package com.group6.accommodation.domain.reservation.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationListItemDto {

    private Long id;
    private String accommodationTitle;
    private String roomTitle;
    private String thumbnail;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer price;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
}
