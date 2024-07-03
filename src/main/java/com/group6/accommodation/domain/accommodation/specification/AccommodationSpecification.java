package com.group6.accommodation.domain.accommodation.specification;

import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.reservation.model.entity.ReservationEntity;
import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class AccommodationSpecification {

    public static Specification<AccommodationEntity> withArea(String area) {
        return (root, query, cb) ->
                area == null ? cb.conjunction() : cb.equal(root.get("areacode"), area);
    }

    public static Specification<AccommodationEntity> withDateRange(LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) -> {
            if (startDate == null || endDate == null) {
                return cb.conjunction();
            }

            LocalDate endDateMinusOne = endDate.minusDays(1);
            LocalDate startDatePlusOne = startDate.plusDays(1);

            Join<AccommodationEntity, RoomEntity> roomJoin = root.join("rooms", JoinType.LEFT);
            Subquery<Long> reservationSubquery = query.subquery(Long.class);
            Root<ReservationEntity> reservationRoot = reservationSubquery.from(ReservationEntity.class);

            reservationSubquery.select(cb.count(reservationRoot))
                    .where(cb.and(
                            cb.equal(reservationRoot.get("room"), roomJoin),
                            cb.isNull(reservationRoot.get("deletedAt")),
                            cb.or(
                                    cb.between(reservationRoot.get("startDate"), startDate, endDateMinusOne),
                                    cb.between(reservationRoot.get("endDate"), startDatePlusOne, endDate),
                                    cb.and(
                                            cb.lessThanOrEqualTo(reservationRoot.get("startDate"), startDate),
                                            cb.greaterThanOrEqualTo(reservationRoot.get("endDate"), endDate)
                                    )
                            )
                    ));

            return cb.lessThan(reservationSubquery, roomJoin.get("roomCount"));
        };
    }

    public static Specification<AccommodationEntity> withHeadcount(Integer headcount) {
        return (root, query, cb) -> {
            if (headcount == null) {
                return cb.conjunction();
            }
            Join<AccommodationEntity, RoomEntity> roomJoin = root.join("rooms", JoinType.LEFT);
            return cb.greaterThanOrEqualTo(roomJoin.get("roomMaxCount"), headcount);
        };
    }

    public static Specification<AccommodationEntity> withDateRangeAndHeadcount(LocalDate startDate, LocalDate endDate, Integer headcount) {
        return (root, query, cb) -> {
            if (startDate == null || endDate == null || headcount == null) {
                return cb.conjunction();
            }

            LocalDate endDateMinusOne = endDate.minusDays(1);
            LocalDate startDatePlusOne = startDate.plusDays(1);

            Join<AccommodationEntity, RoomEntity> roomJoin = root.join("rooms", JoinType.LEFT);
            Subquery<Long> reservationSubquery = query.subquery(Long.class);
            Root<ReservationEntity> reservationRoot = reservationSubquery.from(ReservationEntity.class);

            reservationSubquery.select(cb.count(reservationRoot))
                    .where(cb.and(
                            cb.equal(reservationRoot.get("room"), roomJoin),
                            cb.isNull(reservationRoot.get("deletedAt")),
                            cb.or(
                                    cb.between(reservationRoot.get("startDate"), startDate, endDateMinusOne),
                                    cb.between(reservationRoot.get("endDate"), startDatePlusOne, endDate),
                                    cb.and(
                                            cb.lessThanOrEqualTo(reservationRoot.get("startDate"), startDate),
                                            cb.greaterThanOrEqualTo(reservationRoot.get("endDate"), endDate)
                                    )
                            )
                    ));

            // 예약 가능한 객실 수 계산
            Expression<Long> availableRooms = cb.diff(roomJoin.get("roomCount"), reservationSubquery);

            return cb.and(
                    cb.greaterThan(availableRooms, 0L), // 예약 가능한 객실이 있어야 함
                    cb.greaterThanOrEqualTo(roomJoin.get("roomMaxCount"), headcount), // 객실의 최대 수용 인원이 요청된 인원 이상이어야 함
                    cb.greaterThan(cb.sum(availableRooms, reservationSubquery), 0L) // 전체 객실 수가 0보다 커야 함 (추가 안전 장치)
            );
        };
    }

    public static Specification<AccommodationEntity> withAvailableRooms() {
        return (root, query, cb) -> {
            Join<AccommodationEntity, RoomEntity> roomJoin = root.join("rooms", JoinType.LEFT);
            return cb.greaterThan(roomJoin.get("roomCount"), 0);
        };
    }

    public static Specification<AccommodationEntity> withDistinctAndGroupBy() {
        return (root, query, cb) -> {
            query.distinct(true);
            if (query instanceof CriteriaQuery) {
                ((CriteriaQuery<?>) query).groupBy(root.get("id"));
            }
            return cb.conjunction();
        };
    }
}