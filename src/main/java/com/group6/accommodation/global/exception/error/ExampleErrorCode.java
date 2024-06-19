package com.group6.accommodation.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExampleErrorCode implements ErrorCode{

	TEST(HttpStatus.BAD_REQUEST, "예제입니다."),
	ACCOMMODATION_NOT_EXIST(HttpStatus.BAD_REQUEST, "숙소 정보가 없습니다."),
	ALREADY_ADD_LIKE(HttpStatus.BAD_REQUEST, "이미 찜이 등록된 숙소입니다."),
	;

	private final HttpStatus code;
	private final String info;

}
