package com.group6.accommodation.domain.review.repository;

import com.group6.accommodation.domain.review.model.entity.ReviewEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    @Query("SELECT r.user.id FROM ReviewEntity r WHERE r.id = :reviewId")
    Long findUserIdByReviewId(@Param("reviewId") Long reviewId);

    @Query("SELECT r FROM ReviewEntity r WHERE r.user.id = :userId")
    List<ReviewEntity> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT r FROM ReviewEntity r WHERE r.accommodation.id = :accommodationId")
    List<ReviewEntity> findAllByAccommodationId(@Param("accommodationId") Long accommodationId);
}
