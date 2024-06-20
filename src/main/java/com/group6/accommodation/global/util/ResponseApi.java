package com.group6.accommodation.global.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.group6.accommodation.global.exception.type.CustomException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseApi<T> {
    private int resultCode;
    private String resultMessage;
    private T data;

    public static <T> ResponseApi<T> success(HttpStatus status, T data) {
        return ResponseApi.<T>builder()
                .resultCode(status.value())
                .resultMessage(status.getReasonPhrase())
                .data(data)
                .build();
    }

    public static ResponseApi<?> failed(CustomException ex) {
        return ResponseApi.<Error>builder()
            .resultCode(ex.getStatusCode().value())
            .resultMessage(ex.getStatusCode().getReasonPhrase())
            .data(new Error(ex.getInfo()))
            .build();
    }

    private record Error(String message) { }

}