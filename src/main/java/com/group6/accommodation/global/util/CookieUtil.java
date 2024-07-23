package com.group6.accommodation.global.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public final class CookieUtil {

    @Value("${jwt.refresh-expiration-time}")
    private static Long refreshTokenExpireTime;

    private CookieUtil() {
    }

    public static HttpHeaders createRefreshTokenCookie(String refreshToken) {
        ResponseCookie refreshTokenCookie = ResponseCookie
                .from("refreshToken", refreshToken)
                .maxAge(refreshTokenExpireTime)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return headers;
    }

    public static HttpHeaders deleteRefreshTokenCookie() {
        ResponseCookie refreshTokenCookie = ResponseCookie
                .from("refreshToken", "")
                .maxAge(0)
                .path("/")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return headers;
    }
}
