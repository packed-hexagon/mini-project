package com.group6.accommodation.domain.reservation.model.dto;


import com.group6.accommodation.domain.reservation.annotation.StartBeforeEnd;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
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
@StartBeforeEnd(startTime = "startDate", endTime = "endDate")
public class PostReserveRequestDto {

    @NotNull
    @Positive(message = "인원 수는 한명 이상이여야 합니다.")
    private Integer headcount;

    @NotNull
    @FutureOrPresent(message = "시작 날짜는 과거가 될 수 없습니다.")
    private LocalDate startDate;

    @NotNull
    @FutureOrPresent(message = "끝나는 날짜는 과거가 될 수 없습니다.")
    private LocalDate endDate;

    @NotNull
    @PositiveOrZero(message = "가격은 음수가 될 수 없습니다.")
    private Integer price;
}
