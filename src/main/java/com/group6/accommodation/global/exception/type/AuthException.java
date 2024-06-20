package com.group6.accommodation.global.exception.type;

import com.group6.accommodation.global.exception.error.ErrorCode;

public class AuthException extends CustomException{
    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
