package com.group6.accommodation.domain.accommodation.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class OpenapiConfig {

    // Open Api 키값
    @Value("${openapi.api-key}")
    private String apiKey;

    // 숙소 정보 기본 주소
    @Value("${openapi.base-url}")
    private String baseUrl;

    // 객실 정보 기본 주소
    @Value("${openapi.base-url-room}")
    private String baseUrlRoom;
}
