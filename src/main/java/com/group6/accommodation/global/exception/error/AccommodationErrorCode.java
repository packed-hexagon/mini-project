package com.group6.accommodation.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AccommodationErrorCode implements ErrorCode {

    NOT_FOUND_ACCOMMODATION(HttpStatus.NOT_FOUND, "없는 숙박 ID 입니다."),
    NOT_BOTH_AREA_CATEGORY(HttpStatus.BAD_REQUEST, "동시에는 조회할 수 없습니다."),
    NOT_FOUND_CATEGORY(HttpStatus.NOT_FOUND, "없는 숙박 종류 입니다."),
    NOT_FOUND_AREA(HttpStatus.NOT_FOUND, "없는 숙박 위치 입니다."),
    NOT_FOUND_DATA_PAGE(HttpStatus.NOT_FOUND, "데이터가 없는 페이지입니다."),
    ;

    private final HttpStatus code;
    private final String info;
}
