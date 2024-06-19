package com.group6.accommodation.global.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
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
    private Error error;

    public static <T> ResponseApi<T> success(HttpStatus status, T data) {
        return ResponseApi.<T>builder()
                .resultCode(status.value())
                .resultMessage(status.getReasonPhrase())
                .data(data)
                .build();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Error {
        private List<String> errorMessage;
    }
}