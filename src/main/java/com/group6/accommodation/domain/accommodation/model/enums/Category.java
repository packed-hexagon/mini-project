package com.group6.accommodation.domain.accommodation.model.enums;

import lombok.Getter;

@Getter
public enum Category {
    B02010100("관광호텔"),
    B02010500("콘도미니엄"),
    B02010600("유스호스텔"),
    B02010700("펜션"),
    B02010900("모텔"),
    B02011000("민박"),
    B02011100("게스트하우스"),
    B02011200("홈스테이"),
    B02011300("서비스드레지던스"),
    B02011600("한옥");

    private final String description;

    Category(String description) {
        this.description = description;
    }

}
