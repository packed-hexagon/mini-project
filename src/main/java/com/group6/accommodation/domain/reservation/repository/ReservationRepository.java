package com.group6.accommodation.domain.reservation.repository;

import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.reservation.model.entity.ReservationEntity;
import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    Boolean existsByAccommodationAndRoom(AccommodationEntity accommodation, RoomEntity room);

}