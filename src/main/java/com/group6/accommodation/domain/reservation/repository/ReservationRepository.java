package com.group6.accommodation.domain.reservation.repository;

import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.reservation.model.entity.ReservationEntity;
import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    
    @Query("SELECT r FROM ReservationEntity r WHERE r.room = :room AND r.deletedAt IS NULL AND r.user.id <> :userId")
    List<ReservationEntity> findConflictingReservations(
        @Param("room") RoomEntity room,
        @Param("userId") Long userId);


    Integer countByRoom(@Param("roomId") RoomEntity room);

    Page<ReservationEntity> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT COUNT(r) FROM ReservationEntity r " +
        "WHERE r.room.roomId = :roomId " +
        "AND r.deletedAt IS NULL " +
        "AND ((r.startDate <= :checkOut AND r.endDate >= :checkIn) " +
        "OR (r.startDate <= :checkIn AND r.endDate >= :checkOut) " +
        "OR (r.startDate >= :checkIn AND r.endDate <= :checkOut))")
    int countOverlappingReservations(@Param("roomId") Long roomId,
        @Param("checkIn") LocalDate checkIn,
        @Param("checkOut") LocalDate checkOut);


    @Query("SELECT r.room.roomId FROM ReservationEntity r WHERE r.startDate <= :endDate AND r.endDate >= :startDate AND r.deletedAt IS NULL")
    List<Long> findByStartDateBeforeOrEndDateAfter(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}