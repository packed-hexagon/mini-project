package com.group6.accommodation.domain.accommodation.model.enums;

import com.group6.accommodation.global.exception.type.AccommodationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class AreaTest {

    @ParameterizedTest
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
    void getNameByCode_ShouldThrowException_WhenCodeNotFound() {
        assertThrows(AccommodationException.class, () -> Area.getNameByCode("999"));
    }

    @ParameterizedTest
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
    void getCodeByName_ShouldThrowException_WhenNameNotFound() {
        assertThrows(AccommodationException.class, () -> Area.getCodeByName("없는지역"));
    }
}