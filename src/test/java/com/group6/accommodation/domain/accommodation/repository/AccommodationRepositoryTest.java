package com.group6.accommodation.domain.accommodation.repository;

import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.accommodation.service.DatabaseInitializationService;
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


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccommodationRepositoryTest {

    @MockBean
    private DatabaseInitializationService databaseInitializationService;

    @Autowired
    private AccommodationRepository accommodationRepository;

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

}