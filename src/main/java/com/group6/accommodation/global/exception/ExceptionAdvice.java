package com.group6.accommodation.global.exception;

import com.group6.accommodation.global.exception.type.AccommodationException;
import com.group6.accommodation.global.exception.type.ReservationException;
import com.group6.accommodation.global.exception.type.UserLikeException;
import com.group6.accommodation.global.exception.type.ValidException;
import com.group6.accommodation.global.util.ResponseApi;
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

		StringBuilder errMessage = new StringBuilder();
		for (FieldError error : result.getFieldErrors()) {
			errMessage.append("[")
					.append(error.getField())
					.append("]")
					.append(": ")
					.append(error.getDefaultMessage());
		}
		ValidException validException = new ValidException(HttpStatus.BAD_REQUEST, errMessage.toString());

		log.warn(validException.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseApi.failed(validException));

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
