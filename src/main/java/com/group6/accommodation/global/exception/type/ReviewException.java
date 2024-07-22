package com.group6.accommodation.global.exception.type;

import com.group6.accommodation.global.exception.error.ErrorCode;

public class ReviewException extends CustomException {
    public ReviewException(ErrorCode errorCode) { super(errorCode); }
}
