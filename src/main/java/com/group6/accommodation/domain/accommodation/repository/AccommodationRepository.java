package com.group6.accommodation.domain.accommodation.repository;

import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface AccommodationRepository extends JpaRepository<AccommodationEntity, Long> {

    Page<AccommodationEntity> findByIdIn(List<Long> ids, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE AccommodationEntity a SET a.likeCount = a.likeCount + 1 WHERE a.id = :accommodationId")
    void incrementLikeCount(@Param("accommodationId")Long accommodationId);

    @Transactional
    @Modifying
    @Query("UPDATE AccommodationEntity a SET a.likeCount = a.likeCount - 1 WHERE a.id = :accommodationId")
    void decrementLikeCount(@Param("accommodationId")Long accommodationId);
}
