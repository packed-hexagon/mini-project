package com.group6.accommodation.domain.accommodation.model.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AccommodationResponseDtoTest {
    
    @Test
    @DisplayName("Dto 생성되는지 확인하는 테스트")
    void createDto() {

        // given
        AccommodationResponseDto dto = AccommodationResponseDto.builder()
                .id(1234L)
                .title("숙박명")
                .address("경기도 광명시")
                .address2("")
                .area("서울")
                .category("한옥")
                .image("image.jpg")
                .thumbnail("thumb.jpg")
                .price(150000)
                .latitude(37.6)
                .longitude(128.33)
                .likeCount(0)
                .rating(3.5)
                .build();

        // when, then
        Assertions.assertThat(dto.getArea()).isEqualTo("서울");
        Assertions.assertThat(dto.getCategory()).isEqualTo("한옥");
    }

}