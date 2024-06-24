package com.group6.accommodation.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReservationErrorCode implements ErrorCode{

    NOT_FOUND_ACCOMMODATION(HttpStatus.BAD_REQUEST, "없는 숙박 ID 입니다."),
    NOT_FOUND_ROOM(HttpStatus.BAD_REQUEST, "없는 객실 ID 입니다."),
    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "없는 유저 ID 입니다."),
    NOT_FOUND_RESERVATION(HttpStatus.BAD_REQUEST, "없는 예약 ID 입니다."),
    ALREADY_RESERVED(HttpStatus.BAD_REQUEST, "이미 예약된 객실입니다."),
    FULL_ROOM(HttpStatus.BAD_REQUEST, "예약 가능한 객실이 없습니다."),
    ALREADY_CANCEL(HttpStatus.BAD_REQUEST, "이미 취소된 예약입니다."),
    NOT_MATCH_PRICE(HttpStatus.BAD_REQUEST, "금액이 올바르지 않습니다."),
    ;

    private final HttpStatus code;
    private final String info;

}
