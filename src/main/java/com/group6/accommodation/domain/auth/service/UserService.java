package com.group6.accommodation.domain.auth.service;

import com.group6.accommodation.domain.auth.model.dto.UserRequestDto;
import com.group6.accommodation.domain.auth.model.dto.UserResponseDto;
import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import com.group6.accommodation.domain.auth.repository.UserRepository;
import com.group6.accommodation.global.exception.error.AuthErrorCode;
import com.group6.accommodation.global.exception.type.AuthException;
import com.group6.accommodation.global.redis.repository.RefreshTokenRepository;
import com.group6.accommodation.global.util.ResponseApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.group6.accommodation.global.security.filter.JwtFilter;
import com.group6.accommodation.global.security.token.model.dto.LoginTokenResponseDto;
import com.group6.accommodation.global.security.token.provider.TokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    @Value("${jwt.refresh-expiration-time}")
    private Long refreshTokenExpireTime;

    public UserResponseDto getUserInfo(Long userId) {
        UserEntity result = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException(AuthErrorCode.NOT_FOUNT_USER_BY_USER_ID));

        return UserResponseDto.toResponse(result);
    }

    public UserResponseDto register(UserRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthException(AuthErrorCode.ALREADY_EXIST_EMAIL);
        }

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new AuthException(AuthErrorCode.ALREADY_EXIST_PHONE_NUMBER);
        }

        String encryptedPassword = encodePassword(request.getPassword());
        UserEntity result = userRepository.save(request.toEntity(encryptedPassword));

        return UserResponseDto.toResponse(result);
    }

    public HttpHeaders logout(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new AuthException(AuthErrorCode.NOT_FOUNT_USER_BY_USER_ID);
        }

        if (refreshTokenRepository.existsById(userId)) {
            refreshTokenRepository.deleteById(userId);
        } else {
            throw new AuthException(AuthErrorCode.ALREADY_LOGOUT);
        }

        ResponseCookie refreshTokenCookie = deleteRefreshTokenCookie();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return headers;
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public ResponseApi<LoginTokenResponseDto> refreshTokens(String bearerAccessToken, String refreshToken) {
        String accessToken = resolveBearerAccessToken(bearerAccessToken);
        if (tokenProvider.isTokenExpired(accessToken)) {
            LoginTokenResponseDto refreshTokens = tokenProvider.getRefreshTokens(refreshToken);
            return ResponseApi.success(HttpStatus.OK, refreshTokens);
        } else {
            throw new AuthException(AuthErrorCode.INVALID_TOKEN);
        }
    }

    private String resolveBearerAccessToken(String bearerAccessToken) {
        try {
            if (!StringUtils.hasText(bearerAccessToken)) {
                throw new AuthException(AuthErrorCode.EMPTY_TOKEN);
            }
            if (!bearerAccessToken.startsWith(JwtFilter.TOKEN_PREFIX)) {
                throw new AuthException(AuthErrorCode.UNSUPPORTED_TOKEN_TYPE);
            }
            return bearerAccessToken.substring(JwtFilter.TOKEN_PREFIX.length());
        } catch (AuthException e) {
            throw new AuthException(AuthErrorCode.UNKNOWN_AUTH_ERROR);
        }
    }

    public HttpHeaders createRefreshTokenCookie(String refreshToken) {
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

    private ResponseCookie deleteRefreshTokenCookie() {
        return ResponseCookie
                .from("refreshToken", "")
                .maxAge(0)
                .path("/api")
                .build();
    }
}
