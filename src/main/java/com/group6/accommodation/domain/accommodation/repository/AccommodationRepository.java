package com.group6.accommodation.domain.accommodation.repository;

import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface AccommodationRepository extends JpaRepository<AccommodationEntity, Long>, JpaSpecificationExecutor<AccommodationEntity> {
    Page<AccommodationEntity> findByAreacode(String areaCode, Pageable pageable);
    Page<AccommodationEntity> findByCategory(String categoryCode, Pageable pageable);

    // 숙소명이나 주소에 키워드를 포함한 숙소 조회
    @Query("SELECT a FROM AccommodationEntity a WHERE a.title LIKE %:keyword% OR a.address LIKE %:keyword%")
    Page<AccommodationEntity> findByTitleOrAddressContainingKeyword(@Param("keyword") String keyword, Pageable pageable);
    Page<AccommodationEntity> findByIdIn(List<Long> ids, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE AccommodationEntity a SET a.likeCount = a.likeCount + 1 WHERE a.id = :accommodationId")
    void incrementLikeCount(@Param("accommodationId")Long accommodationId);

    @Transactional
    @Modifying
    @Query("UPDATE AccommodationEntity a SET a.likeCount = a.likeCount - 1 WHERE a.id = :accommodationId")
    void decrementLikeCount(@Param("accommodationId")Long accommodationId);

    @Override
    Optional<AccommodationEntity> findById(Long aLong);

    @Query(value = "SELECT DISTINCT a FROM AccommodationEntity a LEFT JOIN FETCH a.rooms r WHERE a IN :accommodations",
            countQuery = "SELECT COUNT(DISTINCT a) FROM AccommodationEntity a LEFT JOIN a.rooms r WHERE a IN :accommodations")
    Page<AccommodationEntity> findAllWithCountQuery(@Param("accommodations") List<AccommodationEntity> accommodations, Pageable pageable);
}
