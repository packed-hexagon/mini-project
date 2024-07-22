package com.group6.accommodation.domain.review.model.entity;

import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import com.group6.accommodation.domain.reservation.model.entity.ReservationEntity;
import com.group6.accommodation.global.model.entity.TimeStamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Table(name = "review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewEntity extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", referencedColumnName = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="accommodation_id", referencedColumnName = "accommodation_id")
    private AccommodationEntity accommodation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reservation_id", referencedColumnName = "reservation_id")
    private ReservationEntity reservation;

    @Setter
    @Column(name = "rating", nullable = false)
    private int rating;

    @Setter
    @Column(name = "comment", nullable = false)
    private String comment;

    @Setter
    @Column(name = "images", nullable = false)
    private String images;

    @Setter
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    public ReviewEntity(UserEntity user, AccommodationEntity accommodation, ReservationEntity reservation, int rating, String comment, String images) {
        this.user = user;
        this.accommodation = accommodation;
        this.reservation = reservation;
        this.rating = rating;
        this.comment = comment;
        this.images = images;
    }

}
