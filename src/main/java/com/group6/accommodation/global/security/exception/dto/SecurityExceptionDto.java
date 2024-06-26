package com.group6.accommodation.global.security.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class SecurityExceptionDto<T> {
    private int resultCode;
    private String resultMessage;
    private T data;
}
