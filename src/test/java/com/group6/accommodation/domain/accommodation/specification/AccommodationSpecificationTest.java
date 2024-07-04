package com.group6.accommodation.domain.accommodation.specification;

import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.accommodation.repository.AccommodationRepository;
import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import jakarta.persistence.criteria.JoinType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccommodationSpecificationTest {

    @Autowired
    private AccommodationRepository accommodationRepository;

    @Test
    @DisplayName("지역 Specification 테스트")
    void testWithArea() {
        // Given
        String areacode = "1";
        Specification<AccommodationEntity> spec = AccommodationSpecification.withArea(areacode);

        // When
        List<AccommodationEntity> result = accommodationRepository.findAll(spec);

        // Then
        assertFalse(result.isEmpty());
        result.forEach(accommodation -> assertEquals(areacode, accommodation.getAreacode()));
    }

    @Test
    @DisplayName("날짜 범위 Specification 테스트")
    void testWithDateRange() {
        // Given
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(4);
        Specification<AccommodationEntity> spec = AccommodationSpecification.withDateRange(startDate, endDate);

        // When
        List<AccommodationEntity> result = accommodationRepository.findAll(spec);

        // Then
        assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("인원수 Specification 테스트")
    void testWithHeadcount() {
        // Given
        Integer headcount = 2;
        Specification<AccommodationEntity> spec = AccommodationSpecification.withHeadcount(headcount);

        // When
        List<AccommodationEntity> result = accommodationRepository.findAll(Specification.where(spec)
                .and((root, query, cb) -> {
                    root.fetch("rooms", JoinType.LEFT);
                    return cb.conjunction();
                }));

        // Then
        assertFalse(result.isEmpty());
        result.forEach(accommodation ->
                assertTrue(accommodation.getRooms().stream()
                        .anyMatch(room -> room.getRoomMaxCount() >= headcount))
        );
    }

    @Test
    @DisplayName("날짜 범위와 인원수 Specification 테스트")
    void testWithDateRangeAndHeadcount() {
        // Given
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(3);
        Integer headcount = 2;
        Specification<AccommodationEntity> spec = AccommodationSpecification.withDateRangeAndHeadcount(startDate, endDate, headcount);

        // When
        List<AccommodationEntity> result = accommodationRepository.findAll(Specification.where(spec)
                .and((root, query, cb) -> {
                    root.fetch("rooms", JoinType.LEFT);
                    root.fetch("reservations", JoinType.LEFT);
                    return cb.conjunction();
                }));

        // Then
        assertFalse(result.isEmpty());

    }

    @Test
    @DisplayName("사용 가능한 객실 Specification 테스트")
    void testWithAvailableRooms() {
        // Given
        Specification<AccommodationEntity> spec = AccommodationSpecification.withAvailableRooms();

        // When
        List<AccommodationEntity> result = accommodationRepository.findAll(Specification.where(spec)
                .and((root, query, cb) -> {
                    root.fetch("rooms", JoinType.LEFT);
                    return cb.conjunction();
                }));

        // Then
        assertFalse(result.isEmpty());
        result.forEach(accommodation ->
                assertTrue(accommodation.getRooms().stream()
                        .anyMatch(room -> room.getRoomCount() > 0))
        );
    }
}
