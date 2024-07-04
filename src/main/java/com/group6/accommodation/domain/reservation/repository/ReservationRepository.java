package com.group6.accommodation.domain.reservation.repository;

import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.reservation.model.entity.ReservationEntity;
import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    Boolean existsByAccommodationAndRoomAndDeletedAtNotNullAndUserIdNot(AccommodationEntity accommodation, RoomEntity room, Long userId);

    @Query("SELECT COUNT(r) FROM ReservationEntity r WHERE r.room.roomId = :roomId AND r.deletedAt IS NULL AND r.endDate > CURRENT_DATE")
    Integer countByRoom(@Param("roomId") Long roomId);

    Page<ReservationEntity> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("select r.room.roomId from ReservationEntity r where not r.endDate < ?1 or r.startDate > ?2")
    List<Long> findByStartDateBeforeOrEndDateAfter(LocalDate startDate, LocalDate endDate);

}