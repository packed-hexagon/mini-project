package com.group6.accommodation.global.exception.type;

import com.group6.accommodation.global.exception.error.ErrorCode;
import java.util.Map;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

@Getter
public class CustomException extends HttpStatusCodeException {
	private final HttpStatus statusCode;
	private final String info;

	public CustomException(ErrorCode errorCode) {
		super(errorCode.getCode(), errorCode.getInfo());
		this.statusCode = errorCode.getCode();
		this.info = errorCode.getInfo();
	}

	public CustomException(HttpStatus statusCode, Map<String, String> info) {
        super(statusCode, info.toString());
        this.statusCode = statusCode;
		this.info = info.toString();
	}
}
