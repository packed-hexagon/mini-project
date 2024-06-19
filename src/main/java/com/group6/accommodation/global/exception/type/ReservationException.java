package com.group6.accommodation.global.exception.type;

import com.group6.accommodation.global.exception.error.ErrorCode;
import org.springframework.web.client.HttpStatusCodeException;

public class ReservationException extends HttpStatusCodeException {
    public ReservationException(ErrorCode errorCode) {
        super(errorCode.getCode(), errorCode.getInfo());
    }
}
