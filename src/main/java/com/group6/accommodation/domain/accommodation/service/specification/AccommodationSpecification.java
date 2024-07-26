package com.group6.accommodation.domain.accommodation.service.specification;

import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.accommodation.model.enums.Area;
import com.group6.accommodation.domain.accommodation.service.command.AccommodationSearchCommand;
import com.group6.accommodation.domain.reservation.model.entity.ReservationEntity;
import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import jakarta.persistence.criteria.*;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

@UtilityClass
public class AccommodationSpecification {

    public static Specification<AccommodationEntity> findAvailableAccommodation(AccommodationSearchCommand command) {
        return Specification
                .where(withDistinctAndGroupBy())
                .and(withArea(command.getArea()))
                .and(withDateRange(command.getStartDate(), command.getEndDate()))
                .and(withHeadcount(command.getHeadcount()))
                .and(withAvailableRooms());
    }

    public static Specification<AccommodationEntity> withArea(String area) {
        return (root, query, cb) -> {
            if(area == null || area.isEmpty()) {
                return cb.conjunction();
            }
            String areaCode = Area.getCodeByName(area);
            return cb.equal(root.get("areacode"), areaCode);
        };
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

            return cb.lessThan(reservationSubquery, roomJoin.get("count"));
        };
    }

    public static Specification<AccommodationEntity> withHeadcount(Integer headcount) {
        return (root, query, cb) -> {
            if (headcount == null) {
                return cb.conjunction();
            }
            Join<AccommodationEntity, RoomEntity> roomJoin = root.join("rooms", JoinType.LEFT);
            return cb.greaterThanOrEqualTo(roomJoin.get("maxHeadCount"), headcount);
        };
    }

    public static Specification<AccommodationEntity> withAvailableRooms() {
        return (root, query, cb) -> {
            Join<AccommodationEntity, RoomEntity> roomJoin = root.join("rooms", JoinType.LEFT);
            return cb.greaterThan(roomJoin.get("count"), 0);
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