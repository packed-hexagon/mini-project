package com.group6.accommodation.domain.accommodation.service.command;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class AccommodationSearchCommand {
    private final String area;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Integer headcount;
    private final int page;
}
