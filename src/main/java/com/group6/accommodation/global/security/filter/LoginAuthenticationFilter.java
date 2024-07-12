package com.group6.accommodation.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group6.accommodation.global.security.exception.dto.SecurityExceptionDto;
import com.group6.accommodation.global.security.token.model.dto.LoginTokenRequestDto;
import com.group6.accommodation.global.security.token.model.dto.LoginTokenResponseDto;
import com.group6.accommodation.global.security.token.provider.TokenProvider;
import com.group6.accommodation.global.util.ResponseApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;

    public LoginAuthenticationFilter(AuthenticationManager authenticationManager, TokenProvider tokenProvider,
                                     ObjectMapper objectMapper) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
        setFilterProcessesUrl("/open-api/user/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            if (!request.getMethod().equals("POST")) {
                throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
            }
            LoginTokenRequestDto loginRequest = objectMapper.readValue(request.getInputStream(),
                    LoginTokenRequestDto.class);

            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), loginRequest.getPassword());
            setDetails(request, authRequest);

            return authenticationManager.authenticate(authRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException {
        LoginTokenResponseDto generatedToken = tokenProvider.createToken(authResult);

        ResponseApi<Map<String, String>> responseBody = ResponseApi.success(HttpStatus.OK, Map.of(
                "accessToken", generatedToken.getAccessToken(),
                "refreshToken", generatedToken.getRefreshToken()
        ));

        Cookie refreshTokenCookie = new Cookie("refreshToken", generatedToken.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);

        response.addCookie(refreshTokenCookie);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(400);
        response.setCharacterEncoding("utf-8");

        Map<String, Object> message = new HashMap<>();

        message.put("message",failed.getMessage());

        SecurityExceptionDto<?> responseData = SecurityExceptionDto.builder()
                .resultCode(HttpStatus.BAD_REQUEST.value())
                .resultMessage(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .data(message)
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(responseData));
    }
}
