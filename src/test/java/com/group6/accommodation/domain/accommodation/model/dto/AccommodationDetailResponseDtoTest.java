package com.group6.accommodation.domain.accommodation.model.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AccommodationDetailResponseDtoTest {

    @Test
    @DisplayName("Dto 생성되는지 확인하는 테스트")
    void createDetailDto() {

        // given
        AccommodationDetailResponseDto detailDto = AccommodationDetailResponseDto.builder()
                .id(1234L)
                .title("숙박명")
                .address("경기도 광명시")
                .address2("")
                .areacode("서울")
                .sigungucode(8)
                .category("한옥")
                .image("image.jpg")
                .thumbnail("thumb.jpg")
                .price(150000)
                .latitude(37.6)
                .longitude(128.33)
                .mlevel(6)
                .tel("010-0000-0000")
                .likeCount(0)
                .rating(3.5)
                .build();

        // when, then
        Assertions.assertThat(detailDto.getAreacode()).isEqualTo("서울");
        Assertions.assertThat(detailDto.getCategory()).isEqualTo("한옥");
    }

}