package com.group6.accommodation.domain.review.model.dto;

import com.group6.accommodation.domain.reservation.annotation.StartBeforeEnd;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class PostReviewRequestDto {

    @NotNull
    private int rating;

    @NotBlank
    private String comment;

    @NotBlank
    private String images;

}
