package com.group6.accommodation.domain.accommodation.model.enums;

import com.group6.accommodation.global.exception.error.AccommodationErrorCode;
import com.group6.accommodation.global.exception.type.AccommodationException;
import lombok.Getter;

@Getter
public enum Category {
    HOTEL("B02010100", "호텔"),
    CONDO("B02010500", "콘도미니엄"),
    HOSTEL("B02010600", "유스호스텔"),
    PENSION("B02010700", "펜션"),
    MOTEL("B02010900", "모텔"),
    MINBAK("B02011000", "민박"),
    GUESTHOUSE("B02011100", "게스트하우스"),
    HOMESTAY("B02011200", "홈스테이"),
    RESIDENCE("B02011300", "서비스드레지던스"),
    HANOK("B02011600", "한옥"),
    RESORT("A02020200", "리조트"),
    TRAIN("A03020200", "수련원"),
    CAMP("A03021700", "캠핑장");

    private final String code;
    private final String name;

    Category(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(String code) {
        for (Category category : values()) {
            if (category.code.equals(code)) {
                return category.name;
            }
        }
        return null;
    }

    public static String getCodeByName(String name) {
        for (Category category : values()) {
            if (category.name.equals(name)) {
                return category.code;
            }
        }
        return null;
    }

    public static boolean isValidCategoryName(String name) {
        for (Category category : values()) {
            if (category.name.equals(name)) {
                return true;
            }
        }
        return false;
    }
}
