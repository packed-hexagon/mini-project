package com.group6.accommodation.global.exception.type;

import java.util.Map;
import org.springframework.http.HttpStatus;

public class ValidException extends CustomException {
    public ValidException(HttpStatus statusCode, Map<String, String> info){
        super(statusCode, info);
    }
}
