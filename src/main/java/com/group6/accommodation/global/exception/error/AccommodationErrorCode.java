package com.group6.accommodation.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AccommodationErrorCode implements ErrorCode {

    NOT_FOUND_ACCOMMODATION(HttpStatus.NOT_FOUND, "없는 숙박 ID 입니다."),
    ;

    private final HttpStatus code;
    private final String info;
}
