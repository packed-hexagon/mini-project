package com.group6.accommodation.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements ErrorCode {

    RESERVATION_NOT_EXIST(HttpStatus.NOT_FOUND, "예약 정보가 없습니다."),
    NOT_RESERVED_BY_USER(HttpStatus.BAD_REQUEST, "회원님의 예약건이 아닙니다."),
    NOT_REVIEWED_BY_USER(HttpStatus.BAD_REQUEST, "회원님의 리뷰가 아닙니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인 후 이용해주세요."),
    REVIEW_NOT_EXIST(HttpStatus.NOT_FOUND, "리뷰가 없습니다."),
    ;

    private final HttpStatus code;
    private final String info;
}
