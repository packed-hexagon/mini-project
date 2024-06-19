package com.group6.accommodation.global.exception;

import com.group6.accommodation.global.exception.type.ExampleException;
import com.group6.accommodation.global.util.Response;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

	@ExceptionHandler(ExampleException.class)
	public Response<Object> exampleException(ExampleException ex) {
		log.warn("example exception");
		return ExceptionResponse.createErrorResponse(List.of(ex.getMessage()),
			ex.getStatusCode());
	}
}
