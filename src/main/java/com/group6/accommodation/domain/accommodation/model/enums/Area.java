package com.group6.accommodation.domain.accommodation.model.enums;

import com.group6.accommodation.global.exception.error.AccommodationErrorCode;
import com.group6.accommodation.global.exception.type.AccommodationException;
import lombok.Getter;

@Getter
public enum Area {
    SEOUL("1", "서울"),
    INCHEON("2", "인천"),
    DAEJEON("3", "대전"),
    DAEGU("4", "대구"),
    GWANGJU("5", "광주"),
    BUSAN("6", "부산"),
    ULSAN("7", "울산"),
    SEJONG("8", "세종"),
    GYEONGGI("31", "경기"),
    GANGWON("32", "강원"),
    CHUNGBUK("33", "충북"),
    CHUNGNAM("34", "충남"),
    GYEONGBUK("35", "경북"),
    GYEONGNAM("36", "경남"),
    JEONBUK("37", "전북"),
    JEONNAM("38", "전남"),
    JEJU("39", "제주");

    private final String code;
    private final String name;

    Area(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(String code) {
        for (Area area : values()) {
            if (area.code.equals(code)) {
                return area.name;
            }
        }
        return null;
    }

    public static String getCodeByName(String name) {
        for (Area area : values()) {
            if (area.name.equals(name)) {
                return area.code;
            }
        }
        return null;
    }

    public static boolean isValidAreaName(String name) {
        for (Area area : values()) {
            if (area.name.equals(name)) {
                return true;
            }
        }
        return false;
    }
}