package com.group6.accommodation.domain.review.service;

import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.accommodation.repository.AccommodationRepository;
import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import com.group6.accommodation.domain.auth.repository.UserRepository;
import com.group6.accommodation.domain.reservation.model.entity.ReservationEntity;
import com.group6.accommodation.domain.reservation.repository.ReservationRepository;
import com.group6.accommodation.domain.review.model.dto.PostReviewRequestDto;
import com.group6.accommodation.domain.review.model.dto.ReviewResponseDto;
import com.group6.accommodation.domain.review.model.entity.ReviewEntity;
import com.group6.accommodation.domain.review.repository.ReviewRepository;
import com.group6.accommodation.global.exception.error.ReviewErrorCode;
import com.group6.accommodation.global.exception.type.ReviewException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final AccommodationRepository accommodationRepository;

    @Transactional
    public ReviewResponseDto addReview(Long userId, Long reservationId, PostReviewRequestDto requestDto) {

        // reservationId 검증
        ReservationEntity reservation = getResesrvationById(reservationId);

        // 로그인 여부 확인
        UserEntity user = checkUser(userId);

        // reservation의 예약자가 로그인한 계정과 동일한지 검증
        Long reservationUserId = reservationRepository.findUserIdByReservationId(reservationId);

        if(!userId.equals(reservationUserId)) {
            throw new ReviewException(ReviewErrorCode.NOT_RESERVED_BY_USER);
        }

        AccommodationEntity accommodation = reservation.getRoom().getAccommodation();

        // 리뷰 작성
        ReviewEntity review = ReviewEntity.builder()
            .user(user)
            .accommodation(accommodation)
            .reservation(reservation)
            .rating(requestDto.getRating())
            .comment(requestDto.getComment())
            .images(requestDto.getImages())
            .build()
            ;
        reviewRepository.save(review);

        // 숙소의 별점 수정
        double beforeRating = accommodation.getTotalRating();
        int reviewCount = accommodation.getReviewCount();

        double afterRating = ((beforeRating * reviewCount) + requestDto.getRating()) / (reviewCount + 1);

        accommodationRepository.updateRating(accommodation.getId(), afterRating);

        // 숙소에 리뷰 카운트 올리기
        accommodationRepository.incrementReviewCount(reservationId);

        return ReviewResponseDto.toDto(review);
    }

    public List<ReviewResponseDto> getMyReviews(long userId) {
        checkUser(userId);
        List<ReviewEntity> reviews = reviewRepository.findAllByUserId(userId);
        return reviews.stream().map(ReviewResponseDto::toDto).collect(Collectors.toList());
    }

    public List<ReviewResponseDto> getReviewsByAccommodation(Long accommodationId) {
        List<ReviewEntity> reviews = reviewRepository.findAllByAccommodationId(accommodationId);
        return reviews.stream().map(ReviewResponseDto::toDto).collect(Collectors.toList());
    }

    @Transactional
    public ReviewResponseDto updateReview(
        long userId, Long reviewId, PostReviewRequestDto requestDto
    ) {
        // 리뷰 객체 확인
        ReviewEntity review = getReviewById(reviewId);

        // 로그인 여부 확인
        checkUser(userId);

        // review의 예약자가 로그인한 계정과 동일한지 검증
        checkReviewerAndUser(userId, reviewId);

        // 리뷰 수정
        review.setRating(requestDto.getRating());
        review.setComment(requestDto.getComment());
        review.setImages(requestDto.getImages());

        reviewRepository.save(review);

        // 별점 수정 위해 저장
        double beforeRating = review.getRating();

        updateAccommodationRating(review.getAccommodation(), beforeRating, requestDto.getRating());

        return ReviewResponseDto.toDto(review);
    }

    @Transactional
    public String deleteReview(long userId, Long reviewId) {
        // 리뷰 객체 확인
        ReviewEntity review = getReviewById(reviewId);

        // 로그인 여부 확인
        checkUser(userId);

        // review의 예약자가 로그인한 계정과 동일한지 검증
        checkReviewerAndUser(userId, reviewId);

        // review 삭제
        reviewRepository.delete(review);

        AccommodationEntity accommodation = review.getAccommodation();
        int deletedRating = review.getRating();

        // accommodation rating 수정
        double totalRating = accommodation.getTotalRating();
        int reviewCount = accommodation.getReviewCount();
        double updatedRating = reviewCount > 1
            ? ((totalRating * reviewCount) - deletedRating) / (reviewCount - 1)
            : 0.0;
        accommodationRepository.updateRating(accommodation.getId(), updatedRating);
        accommodationRepository.decrementReviewCount(accommodation.getId());
        return "Delete Success";
    }


    private ReservationEntity getResesrvationById(long reservationId) {
        return reservationRepository.findById(reservationId)
            .orElseThrow(() -> new ReviewException(ReviewErrorCode.RESERVATION_NOT_EXIST));
    }

    private UserEntity checkUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new ReviewException(ReviewErrorCode.UNAUTHORIZED));
    }

    private void checkReviewerAndUser (Long userId, Long reviewId) {
        Long reviewUserId = reviewRepository.findUserIdByReviewId(reviewId);

        if(!userId.equals(reviewUserId)) {
            throw new ReviewException(ReviewErrorCode.NOT_REVIEWED_BY_USER);
        }
    }

    private ReviewEntity getReviewById(long reviewId) {
        return reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ReviewException(ReviewErrorCode.REVIEW_NOT_EXIST));
    }

    private void updateAccommodationRating(AccommodationEntity accommodation, double beforeRating, double newRating) {
        double totalRating = accommodation.getTotalRating();
        int reviewCount = accommodation.getReviewCount();
        double updatedRating = ((totalRating * reviewCount) - beforeRating + newRating) / reviewCount;
        accommodationRepository.updateRating(accommodation.getId(), updatedRating);
    }
}
