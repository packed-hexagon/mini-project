package com.group6.accommodation.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    USER_REGISTER_REQUEST_IS_NULL(HttpStatus.BAD_REQUEST, "모든 값을 넣어주세요.")
    ;

    private final HttpStatus code;
    private final String info;

}

