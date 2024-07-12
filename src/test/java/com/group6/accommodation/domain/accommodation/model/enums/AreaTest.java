package com.group6.accommodation.domain.accommodation.model.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class AreaTest {

    @ParameterizedTest
    @DisplayName("유효한 지역 코드일 경우")
    @CsvSource({
            "1, 서울",
            "2, 인천",
            "3, 대전",
            "4, 대구",
            "5, 광주",
            "6, 부산",
            "7, 울산",
            "8, 세종",
            "31, 경기",
            "32, 강원",
            "33, 충북",
            "34, 충남",
            "35, 경북",
            "36, 경남",
            "37, 전북",
            "38, 전남",
            "39, 제주"
    })
    void getNameByCode_ShouldReturnCorrectName(String code, String expectedName) {
        assertEquals(expectedName, Area.getNameByCode(code));
    }

    @Test
    @DisplayName("유효하지 않은 지역 코드일 경우")
    void getNameByCode_ShouldThrowException_WhenCodeNotFound() {
        assertNull(Area.getNameByCode("999"));
    }

    @ParameterizedTest
    @DisplayName("유효한 숙소 지역명일 경우")
    @CsvSource({
            "서울, 1",
            "인천, 2",
            "대전, 3",
            "대구, 4",
            "광주, 5",
            "부산, 6",
            "울산, 7",
            "세종, 8",
            "경기, 31",
            "강원, 32",
            "충북, 33",
            "충남, 34",
            "경북, 35",
            "경남, 36",
            "전북, 37",
            "전남, 38",
            "제주, 39"
    })
    void getCodeByName_ShouldReturnCorrectCode(String name, String expectedCode) {
        assertEquals(expectedCode, Area.getCodeByName(name));
    }

    @Test
    @DisplayName("유효하지 않은 숙소 지역명일 경우")
    void getCodeByName_ShouldThrowException_WhenNameNotFound() {
        assertNull(Area.getCodeByName("없는지역"));
    }
}