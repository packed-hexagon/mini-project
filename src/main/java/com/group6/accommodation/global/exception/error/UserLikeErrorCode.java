package com.group6.accommodation.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserLikeErrorCode implements ErrorCode{

    ACCOMMODATION_NOT_EXIST(HttpStatus.BAD_REQUEST, "숙소 정보가 없습니다."),
    ALREADY_ADD_LIKE(HttpStatus.BAD_REQUEST, "이미 찜이 등록된 숙소입니다."),
    ACCOMMODATION_NOT_LIKED(HttpStatus.NOT_FOUND, "찜한 숙박이 아닙니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인 후 이용해주세요."),
    ;

    private final HttpStatus code;
    private final String info;
}
