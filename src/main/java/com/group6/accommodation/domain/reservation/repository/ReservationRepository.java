package com.group6.accommodation.domain.reservation.repository;

import com.group6.accommodation.domain.reservation.model.entity.ReservationEntity;
import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM reservation r WHERE r.accommodation_id = :accommodationId AND r.room_id = :roomId AND r.deleted_at IS NULL AND r.user_id <> :userId", nativeQuery = true)
    Boolean checkIsReservedRoom(@Param("accommodationId") Long accommodationId,
        @Param("roomId") Long roomId,
        @Param("userId") Long userId);



    @Query("SELECT COUNT(r) FROM ReservationEntity r WHERE r.room.roomId = :roomId AND r.deletedAt IS NULL AND r.endDate > CURRENT_DATE")
    Integer countByRoom(@Param("roomId") Long roomId);

    Page<ReservationEntity> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("select r.room.roomId from ReservationEntity r where not r.endDate < ?1 or r.startDate > ?2")
    List<Long> findByStartDateBeforeOrEndDateAfter(LocalDate startDate, LocalDate endDate);

}