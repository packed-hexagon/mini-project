package com.group6.accommodation.global.exception.error;


import org.springframework.http.HttpStatus;

public interface ErrorCode {
	HttpStatus getCode();
	String getInfo();
}
