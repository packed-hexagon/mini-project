package com.group6.accommodation.domain.reservation.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReserveListItemDto {

    private Long id;

    private String accommodationTitle;
    private String roomTitle;
    private String thumbnail;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer price;
    private Instant createdAt;
    private Instant deletedAt;

}
