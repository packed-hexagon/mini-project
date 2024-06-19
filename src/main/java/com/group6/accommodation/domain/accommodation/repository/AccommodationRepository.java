package com.group6.accommodation.domain.accommodation.repository;

import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationRepository extends JpaRepository<AccommodationEntity, Long> {
}
