package com.group6.accommodation.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RoomErrorCode implements ErrorCode{

	NOT_FOUND_ACCOMMODATION(HttpStatus.NOT_FOUND, "없는 숙박 ID 입니다."),
	NOT_FOUND_ROOM(HttpStatus.NOT_FOUND, "없는 객실 ID 입니다."),
	ERROR_URI(HttpStatus.BAD_REQUEST, "잘못된 URI입니다."),
	ERROR_RESTTEMPLATE(HttpStatus.BAD_REQUEST, "RestTemplate에 문제가 생겼습니다."),
	NOT_FOUND_DATA_PAGE(HttpStatus.NOT_FOUND, "데이터가 없는 페이지입니다."),
	JSON_PARSE_ERROR(HttpStatus.BAD_REQUEST, "JSON 파싱 중에 오류가 발생했습니다.")
	;

	private final HttpStatus code;
	private final String info;
}
