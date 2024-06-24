package com.group6.accommodation.domain.accommodation.repository;

import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.accommodation.model.enums.Area;
import com.group6.accommodation.domain.accommodation.model.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccommodationRepository extends JpaRepository<AccommodationEntity, Long> {
    Page<AccommodationEntity> findByAreacode(String areaCode, Pageable pageable);
    Page<AccommodationEntity> findByCategory(String categoryCode, Pageable pageable);

    // 숙소명이나 주소에 키워드를 포함한 숙소 조회
    @Query("SELECT a FROM AccommodationEntity a WHERE a.title LIKE %:keyword% OR a.address LIKE %:keyword%")
    Page<AccommodationEntity> findByTitleOrAddressContainingKeyword(@Param("keyword") String keyword, Pageable pageable);
}
