package com.group6.accommodation.domain.accommodation.model.dto;

import com.group6.accommodation.domain.accommodation.annotation.StartBeforeEndAndNullable;
import com.group6.accommodation.domain.accommodation.annotation.ValidArea;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@StartBeforeEndAndNullable(startTime = "startDate", endTime = "endDate")
public class AccommodationConditionRequestDto {
    @ValidArea
    String area;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @FutureOrPresent(message = "시작날짜가 과거일 수 없다.")
    LocalDate startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @FutureOrPresent(message = "종료날짜가 과거일 수 없다.")
    LocalDate endDate;

    @Min(value = 1, message = "인원 수는 1명 이상이어야 합니다.")
    Integer headcount;

    @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.")
    int page = 0;
}
