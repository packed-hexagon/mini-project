package com.group6.accommodation.domain.reservation.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ReservationRequestParamDto {
    private int page;
    private int size;
    private String direction;
}
