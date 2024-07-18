package com.group6.accommodation.domain.reservation.repository;

import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.reservation.model.dto.ReservationListItemDto;
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

    
    @Query("SELECT r FROM ReservationEntity r WHERE r.room = :accommodation AND r.room = :room AND r.deletedAt IS NULL AND r.user.id <> :userId")
    List<ReservationEntity> findConflictingReservations(
        @Param("accommodation")AccommodationEntity accommodation,
        @Param("room") RoomEntity room,
        @Param("userId") Long userId);


    Integer countByRoom(@Param("roomId") RoomEntity room);

    @Query("""
        SELECT new com.group6.accommodation.domain.reservation.model.dto.ReservationListItemDto(
        re.id, ac.title, ro.title, ac.thumbnail, re.startDate, re.endDate, re.price, re.createdAt, re.deletedAt)
        FROM ReservationEntity re
        LEFT JOIN re.room ro
        LEFT JOIN ro.accommodation ac
        WHERE re.user.id = :userId
    """)
    Page<ReservationListItemDto> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

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