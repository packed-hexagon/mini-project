package com.group6.accommodation.domain.reservation.repository;

import com.group6.accommodation.domain.reservation.model.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

}