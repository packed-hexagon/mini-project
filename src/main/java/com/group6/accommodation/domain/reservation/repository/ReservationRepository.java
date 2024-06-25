package com.group6.accommodation.domain.reservation.repository;

import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.reservation.model.entity.ReservationEntity;
import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    Boolean existsByAccommodationAndRoomAndDeletedAtNotNullAndUserIdNot(AccommodationEntity accommodation, RoomEntity room, Long userId);

    Page<ReservationEntity> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

}