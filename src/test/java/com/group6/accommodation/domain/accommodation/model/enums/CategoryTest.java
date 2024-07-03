package com.group6.accommodation.domain.accommodation.model.enums;

import com.group6.accommodation.global.exception.type.AccommodationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @ParameterizedTest
    @CsvSource({
            "B02010100, 호텔",
            "B02010500, 콘도미니엄",
            "B02010600, 유스호스텔",
            "B02010700, 펜션",
            "B02010900, 모텔",
            "B02011000, 민박",
            "B02011100, 게스트하우스",
            "B02011200, 홈스테이",
            "B02011300, 서비스드레지던스",
            "B02011600, 한옥",
            "A02020200, 리조트",
            "A03020200, 수련원",
            "A03021700, 캠핑장"
    })
    void getNameByCode_ShouldReturnCorrectName(String code, String expectedName) {
        assertEquals(expectedName, Category.getNameByCode(code));
    }

    @Test
    void getNameByCode_ShouldThrowException_WhenCodeNotFound() {
        assertThrows(AccommodationException.class, () -> Category.getNameByCode("INVALID_CODE"));
    }

    @ParameterizedTest
    @CsvSource({
            "호텔, B02010100",
            "콘도미니엄, B02010500",
            "유스호스텔, B02010600",
            "펜션, B02010700",
            "모텔, B02010900",
            "민박, B02011000",
            "게스트하우스, B02011100",
            "홈스테이, B02011200",
            "서비스드레지던스, B02011300",
            "한옥, B02011600",
            "리조트, A02020200",
            "수련원, A03020200",
            "캠핑장, A03021700"
    })
    void getCodeByName_ShouldReturnCorrectCode(String name, String expectedCode) {
        assertEquals(expectedCode, Category.getCodeByName(name));
    }

    @Test
    void getCodeByName_ShouldThrowException_WhenNameNotFound() {
        assertThrows(AccommodationException.class, () -> Category.getCodeByName("없는카테고리"));
    }
}