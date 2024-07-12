package com.group6.accommodation.domain.accommodation.repository;

import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.accommodation.service.DatabaseInitializationService;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccommodationRepositoryTest {

    @MockBean
    private DatabaseInitializationService databaseInitializationService;

    @Autowired
    private AccommodationRepository accommodationRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("지역별 숙소 조회")
    void findByArea() {
        // given
        AccommodationEntity accommodation = AccommodationEntity.builder()
                .id(123456L)
                .title("숙박명")
                .address("경기도 광명시")
                .address2("")
                .areacode("1")
                .sigungucode(3)
                .category("B02011100")
                .image("image.url")
                .thumbnail("thumbnail.jpg")
                .latitude(37.6)
                .longitude(128.33)
                .mlevel(6)
                .tel("010-0000-0000")
                .likeCount(0)
                .rating(4.5)
                .build();

        AccommodationEntity savedAccommodation = accommodationRepository.save(accommodation);

        // when
        Page<AccommodationEntity> response = accommodationRepository.findByAreacode(
                "1", PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "likeCount")));


        // then
        Assertions.assertThat(response.getContent().get(0).getAreacode()).isEqualTo(savedAccommodation.getAreacode());
    }

    @Test
    @DisplayName("테마별 숙소 조회")
    void findByCategory() {
        // given
        AccommodationEntity accommodation = AccommodationEntity.builder()
                .id(123456L)
                .title("숙박명")
                .address("경기도 광명시")
                .address2("")
                .areacode("1")
                .sigungucode(3)
                .category("B02010500")
                .image("image.url")
                .thumbnail("thumbnail.jpg")
                .latitude(37.6)
                .longitude(128.33)
                .mlevel(6)
                .tel("010-0000-0000")
                .likeCount(0)
                .rating(4.5)
                .build();

        AccommodationEntity savedAccommodation = accommodationRepository.save(accommodation);

        // when
        Page<AccommodationEntity> response = accommodationRepository.findByCategory(
                "B02010500", PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "likeCount")));


        // then
        Assertions.assertThat(response.getContent().get(0).getCategory()).isEqualTo(savedAccommodation.getCategory());
    }

    @Test
    @DisplayName("키워드로 숙소 조회")
    void findByKeyword() {
        // given
        AccommodationEntity accommodation1 = AccommodationEntity.builder()
                .id(123456L)
                .title("Lovely")
                .address("경기도 광명시")
                .address2("")
                .areacode("1")
                .sigungucode(3)
                .category("B02010500")
                .image("image.url")
                .thumbnail("thumbnail.jpg")
                .latitude(37.6)
                .longitude(128.33)
                .mlevel(6)
                .tel("010-0000-0000")
                .likeCount(0)
                .rating(4.5)
                .build();

        AccommodationEntity accommodation2 = AccommodationEntity.builder()
                .id(123457L)
                .title("util")
                .address("경기도 광명시")
                .address2("")
                .areacode("1")
                .sigungucode(3)
                .category("B02010500")
                .image("image.url")
                .thumbnail("thumbnail.jpg")
                .latitude(37.6)
                .longitude(128.33)
                .mlevel(6)
                .tel("010-0000-0000")
                .likeCount(0)
                .rating(4.5)
                .build();

        accommodationRepository.save(accommodation1);
        accommodationRepository.save(accommodation2);

        // when
        Pageable pageable = PageRequest.of(0, 10);
        Page<AccommodationEntity> response = accommodationRepository.findByTitleOrAddressContainingKeyword("Lovely", pageable);

        // then
        Assertions.assertThat(response.getContent()).hasSize(1);
        Assertions.assertThat(response.getContent().get(0).getTitle()).isEqualTo("Lovely");
    }

    @Test
    @DisplayName("좋아요 수 증가")
    void incrementLikeCount() {
        // given
        AccommodationEntity accommodation = AccommodationEntity.builder()
                .id(123456L)
                .title("숙박명")
                .address("경기도 광명시")
                .address2("상세주소")
                .areacode("31")  // 경기도
                .sigungucode(610)  // 광명시
                .category("B02010100")  // 콘도미니엄
                .image("image.jpg")
                .thumbnail("thumbnail.jpg")
                .latitude(37.4786)
                .longitude(126.8646)
                .mlevel(6)
                .tel("02-1234-5678")
                .likeCount(0)
                .rating(4.5)
                .build();
        AccommodationEntity savedAccommodation = accommodationRepository.save(accommodation);

        // when
        accommodationRepository.incrementLikeCount(savedAccommodation.getId());
        entityManager.flush(); // 변경사항을 데이터베이스에 즉시 반영
        entityManager.clear(); // 영속성 컨텍스트 초기화

        // then
        AccommodationEntity updatedAccommodation = accommodationRepository.findById(savedAccommodation.getId()).orElseThrow();
        Assertions.assertThat(updatedAccommodation.getLikeCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("좋아요 수 감소")
    void decrementLikeCount() {
        // given
        AccommodationEntity accommodation = AccommodationEntity.builder()
                .id(123457L)
                .title("숙박명2")
                .address("서울특별시 강남구")
                .address2("테헤란로 123")
                .areacode("1")  // 서울
                .sigungucode(680)  // 강남구
                .category("B02010900")  // 호텔
                .image("image2.jpg")
                .thumbnail("thumbnail2.jpg")
                .latitude(37.5065)
                .longitude(127.0537)
                .mlevel(6)
                .tel("02-9876-5432")
                .likeCount(1)
                .rating(4.8)
                .build();
        AccommodationEntity savedAccommodation = accommodationRepository.save(accommodation);

        // when
        accommodationRepository.decrementLikeCount(savedAccommodation.getId());
        entityManager.flush(); // 변경사항을 데이터베이스에 즉시 반영
        entityManager.clear(); // 영속성 컨텍스트 초기화

        // then
        AccommodationEntity updatedAccommodation = accommodationRepository.findById(savedAccommodation.getId()).orElseThrow();
        Assertions.assertThat(updatedAccommodation.getLikeCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("숙소 목록 조회 with 객실 정보")
    void findAllWithCountQuery() {
        // given
        AccommodationEntity accommodation1 = AccommodationEntity.builder()
                .id(123458L)
                .title("숙박명3")
                .address("부산광역시 해운대구")
                .address2("해운대해변로 264")
                .areacode("6")  // 부산
                .sigungucode(26)  // 해운대구
                .category("B02010500")  // 콘도미니엄
                .image("image3.jpg")
                .thumbnail("thumbnail3.jpg")
                .latitude(35.1586)
                .longitude(129.1603)
                .mlevel(6)
                .tel("051-1234-5678")
                .likeCount(50)
                .rating(4.2)
                .build();

        AccommodationEntity accommodation2 = AccommodationEntity.builder()
                .id(123459L)
                .title("숙박명4")
                .address("제주특별자치도 서귀포시")
                .address2("중문관광로 72번길 75")
                .areacode("39")  // 제주도
                .sigungucode(3)  // 서귀포시
                .category("B02010100")  // 관광호텔
                .image("image4.jpg")
                .thumbnail("thumbnail4.jpg")
                .latitude(33.2496)
                .longitude(126.4123)
                .mlevel(6)
                .tel("064-9876-5432")
                .likeCount(100)
                .rating(4.7)
                .build();

        accommodationRepository.saveAll(List.of(accommodation1, accommodation2));

        // when
        Pageable pageable = PageRequest.of(0, 10);
        Page<AccommodationEntity> result = accommodationRepository.findAllWithCountQuery(
                List.of(accommodation1, accommodation2), pageable);

        // then
        Assertions.assertThat(result.getContent()).hasSize(2);
        Assertions.assertThat(result.getContent()).extracting("title")
                .containsExactlyInAnyOrder("숙박명3", "숙박명4");
    }

}