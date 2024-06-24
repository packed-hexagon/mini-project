package com.group6.accommodation.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RoomErrorCode implements ErrorCode{

	NOT_FOUND_ROOM(HttpStatus.NOT_FOUND, "없는 객실 ID 입니다."),
	;

	private final HttpStatus code;
	private final String info;
}
