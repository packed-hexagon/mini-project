package com.group6.accommodation.global.exception;

import com.group6.accommodation.lobal.exception.type.ReservationException;
import com.group6.accommodation.global.util.ResponseApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

	@ExceptionHandler(ReservationException.class)
	public ResponseEntity<ResponseApi<?>> reservationException(ReservationException ex) {
		log.warn(ex.getMessage());
		return ResponseEntity.status(ex.getStatusCode()).body(ResponseApi.failed(ex));
	}

}