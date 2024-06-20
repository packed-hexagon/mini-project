package com.group6.accommodation.domain.reservation.model.dto;


import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostReserveRequestDto {
    private Integer headcount;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer price;
}
