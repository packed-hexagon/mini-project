package com.group6.accommodation.global.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group6.accommodation.global.security.exception.dto.SecurityExceptionDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("utf-8");

        Map<String, String> message = new HashMap<>();
        message.put("message", "로그인 후 이용해주세요.");

        SecurityExceptionDto<?> responseData = SecurityExceptionDto.builder()
                .resultCode(HttpStatus.UNAUTHORIZED.value())
                .resultMessage(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .data(message)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(responseData);

        response.getWriter().write(jsonResponse);
    }
}
