package com.group6.accommodation.global.exception;

import com.group6.accommodation.global.exception.type.AccommodationException;
import com.group6.accommodation.global.exception.type.AuthException;
import com.group6.accommodation.global.exception.type.ReservationException;
import com.group6.accommodation.global.exception.type.UserLikeException;
import com.group6.accommodation.global.exception.type.ValidException;
import com.group6.accommodation.global.util.ResponseApi;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> notValidException(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();

		Map<String, String> errorMessages = new HashMap<>();

		for (FieldError error : result.getFieldErrors()) {
			errorMessages.put(error.getField(), error.getDefaultMessage());
		}
		ValidException validException = new ValidException(HttpStatus.BAD_REQUEST, errorMessages);

		log.warn(validException.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseApi.failed(validException, errorMessages));
	}

	@ExceptionHandler(AuthException.class)
	public ResponseEntity<ResponseApi<?>> authException(AuthException ex) {
		log.warn(ex.getMessage());
		return ResponseEntity.status(ex.getStatusCode()).body(ResponseApi.failed(ex));
	}

	@ExceptionHandler(ReservationException.class)
	public ResponseEntity<ResponseApi<?>> reservationException(ReservationException ex) {
		log.warn(ex.getMessage());
		return ResponseEntity.status(ex.getStatusCode()).body(ResponseApi.failed(ex));
	}

	@ExceptionHandler(AccommodationException.class)
	public ResponseEntity<ResponseApi<?>> accommodationException(AccommodationException ex) {
		log.warn(ex.getMessage());
		return ResponseEntity.status(ex.getStatusCode()).body(ResponseApi.failed(ex));
	}

	@ExceptionHandler(UserLikeException.class)
	public ResponseEntity<ResponseApi<?>> userLikeException(UserLikeException ex) {
		log.warn(ex.getMessage());
		return ResponseEntity.status(ex.getStatusCode()).body(ResponseApi.failed(ex));
	}

}
