package com.group6.accommodation.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExampleErrorCode implements ErrorCode{

	TEST(HttpStatus.BAD_REQUEST, "예제입니다."),
	;

	private final HttpStatus code;
	private final String info;

}
