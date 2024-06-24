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
    MOTEL("B02010800", "모텔"),
    MINBAK("B02010900", "민박"),
    GUESTHOUSE("B02011000", "게스트하우스"),
    HOMESTAY("B02011100", "홈스테이"),
    RESIDENCE("B02011200", "서비스드레지던스"),
    HANOK("B02011300", "한옥"),
    RESORT("A02020200", "리조트");

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
        throw new AccommodationException(AccommodationErrorCode.NOT_FOUND_CATEGORY);
    }

    public static String getCodeByName(String name) {
        for (Category category : values()) {
            if (category.name.equals(name)) {
                return category.code;
            }
        }
        throw new AccommodationException(AccommodationErrorCode.NOT_FOUND_CATEGORY);
    }
}
