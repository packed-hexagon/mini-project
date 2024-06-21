package com.group6.accommodation.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    ALREADY_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
    ALREADY_EXIST_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "이미 존재하는 휴대폰 번호입니다."),
    ;

    private final HttpStatus code;
    private final String info;

}

