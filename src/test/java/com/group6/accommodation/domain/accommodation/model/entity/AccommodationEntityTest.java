package com.group6.accommodation.domain.accommodation.model.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AccommodationEntityTest {

    @Test
    @DisplayName("숙박 정보 생성되는지 확인하는 테스트")
    void createAccommodation() {
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

        // when, then
        Assertions.assertThat(accommodation.getId()).isEqualTo(123456L);
        Assertions.assertThat(accommodation.getAreacode()).isEqualTo("1");
    }

}