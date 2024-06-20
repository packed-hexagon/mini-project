package com.group6.accommodation.global.exception.type;

import org.springframework.http.HttpStatus;

public class ValidException extends CustomException {
    public ValidException(HttpStatus statusCode, String info){
        super(statusCode, info);
    }
}
