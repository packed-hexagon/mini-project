package com.group6.accommodation.domain.review.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import com.group6.accommodation.domain.reservation.model.entity.ReservationEntity;
import com.group6.accommodation.domain.review.model.entity.ReviewEntity;
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
public class ReviewResponseDto {

    private Long reviewId;

    private UserEntity user;

    private AccommodationEntity accommodation;

    private ReservationEntity reservation;

    private int rating;

    private String comment;

    private String images;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    public static ReviewResponseDto toDto(ReviewEntity review) {
        return ReviewResponseDto.builder()
            .reviewId(review.getReviewId())
            .user(review.getUser())
            .accommodation(review.getAccommodation())
            .reservation(review.getReservation())
            .rating(review.getRating())
            .comment(review.getComment())
            .images(review.getImages())
            .createdAt(review.getCreatedAt())
            .updatedAt(LocalDateTime.now())
            .build()
            ;
    }
}
