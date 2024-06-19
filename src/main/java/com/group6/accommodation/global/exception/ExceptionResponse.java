package com.group6.accommodation.global.exception;

import com.group6.accommodation.global.util.Response;
import java.util.List;
import org.springframework.http.HttpStatusCode;

public class ExceptionResponse {

	// exception 응답 구조 생성
	public static Response<Object> createErrorResponse(List<String> errorMessage, HttpStatusCode status) {
		Response.Error error = Response.Error
			.builder()
			.errorMessage(errorMessage)
			.build();

		return Response.builder()
			.resultCode(String.valueOf(status.value()))
			.resultMessage(status.toString())
			.error(error)
			.build();
	}
}