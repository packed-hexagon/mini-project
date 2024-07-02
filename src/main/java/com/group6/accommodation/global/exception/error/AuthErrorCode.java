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
    NOT_FOUNT_USER_BY_USER_ID(HttpStatus.NOT_FOUND, "해당 ID를 가진 유저가 없습니다."),
    INVALID_TOKEN(HttpStatus.NOT_FOUND, "유효하지 않은 토큰입니다."),
    EMPTY_TOKEN(HttpStatus.NOT_FOUND, "토큰이 비어있습니다."),
    EXPIRED_TOKEN(HttpStatus.NOT_FOUND, "토큰이 만료되었습니다."),
    UNSUPPORTED_TOKEN_TYPE(HttpStatus.NOT_FOUND, "올바르지 않은 토큰 인증 방식입니다."),
    UNKNOWN_AUTH_ERROR(HttpStatus.NOT_FOUND, "알 수 없는 토큰 오류입니다."),
    ALREADY_LOGOUT(HttpStatus.BAD_REQUEST, "이미 로그아웃 된 유저입니다."),
    ;

    private final HttpStatus code;
    private final String info;

}

