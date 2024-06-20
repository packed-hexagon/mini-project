package com.group6.accommodation.global.exception.type;

import com.group6.accommodation.global.exception.error.ErrorCode;

public class AccommodationException extends CustomException{
    public AccommodationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
