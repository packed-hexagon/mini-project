package com.group6.accommodation.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    ALREADY_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
    ALREADY_EXIST_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "이미 존재하는 휴대폰 번호입니다."),
    NOT_FOUND_USER_BY_EMAIL(HttpStatus.NOT_FOUND, "해당 이메일을 가진 유저가 없습니다."),
    NOT_MATCH_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 틀렸습니다."),
    ;

    private final HttpStatus code;
    private final String info;

}

