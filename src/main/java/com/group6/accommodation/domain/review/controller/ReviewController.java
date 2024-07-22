package com.group6.accommodation.domain.review.controller;

import com.group6.accommodation.domain.review.model.dto.PostReviewRequestDto;
import com.group6.accommodation.domain.review.model.dto.ReviewResponseDto;
import com.group6.accommodation.domain.review.service.ReviewService;
import com.group6.accommodation.global.security.service.CustomUserDetails;
import com.group6.accommodation.global.util.Response;
import com.group6.accommodation.global.util.ResponseApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
@Tag(name = "Review", description = "리뷰 관련 API")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{reservationId}")
    @Operation(summary = "리뷰 작성하기")
    public ResponseEntity<ResponseApi<ReviewResponseDto>> addReview (
        @AuthenticationPrincipal CustomUserDetails user,
        @PathVariable Long reservationId,
        @RequestBody PostReviewRequestDto requestDto
    ) {
        ReviewResponseDto responseDto = reviewService.addReview(user.getUserId(), reservationId, requestDto);
        ResponseApi<ReviewResponseDto> result = ResponseApi.success(HttpStatus.CREATED, responseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // 내가 쓴 리뷰 조회
    @GetMapping("/user/review")
    @Operation(summary = "내가 쓴 리뷰 조회")
    public ResponseEntity<ResponseApi<List<ReviewResponseDto>>> getMyReviews(
        @AuthenticationPrincipal CustomUserDetails user
    ) {
        List<ReviewResponseDto> responseDtoList = reviewService.getMyReviews(user.getUserId());
        ResponseApi<List<ReviewResponseDto>> result = ResponseApi.success(HttpStatus.OK, responseDtoList);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 숙소 별 리뷰 조회
    @GetMapping("/accommodation/{accommodationId}")
    @Operation(summary = "숙소 별 리뷰 조회")
    public ResponseEntity<ResponseApi<List<ReviewResponseDto>>> getReviewsByAccommodation(
        @PathVariable Long accommodationId
    ) {
        List<ReviewResponseDto> responseDtoList = reviewService.getReviewsByAccommodation(accommodationId);
        ResponseApi<List<ReviewResponseDto>> result = ResponseApi.success(HttpStatus.OK, responseDtoList);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    // 리뷰 수정
    @PutMapping("/{reviewId}")
    @Operation(summary = "리뷰 수정하기")
    public ResponseEntity<ResponseApi<ReviewResponseDto>> updateReview (
        @AuthenticationPrincipal CustomUserDetails user,
        @PathVariable Long reviewId,
        @RequestBody PostReviewRequestDto requestDto
    ) {
        ReviewResponseDto responseDto = reviewService.updateReview(user.getUserId(), reviewId, requestDto);
        ResponseApi<ReviewResponseDto> result = ResponseApi.success(HttpStatus.OK, responseDto);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    // 리뷰 삭제

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "리뷰 삭제하기")
    public ResponseEntity<ResponseApi<String>> deleteReview (
        @AuthenticationPrincipal CustomUserDetails user,
        @PathVariable Long reviewId
    ) {
        String response = reviewService.deleteReview(user.getUserId(), reviewId);
        ResponseApi<String> result = ResponseApi.success(HttpStatus.NO_CONTENT, response);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
