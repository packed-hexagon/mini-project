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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseApi<UserResponseDto> getUserInfo(Long userId) {
        UserEntity result = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException(AuthErrorCode.NOT_FOUNT_USER_BY_USER_ID));

        UserResponseDto response = UserResponseDto.toResponse(result);
        return ResponseApi.success(HttpStatus.OK, response);
    }

    public ResponseApi<UserResponseDto> register(UserRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthException(AuthErrorCode.ALREADY_EXIST_EMAIL);
        }

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new AuthException(AuthErrorCode.ALREADY_EXIST_PHONE_NUMBER);
        }

        String encryptedPassword = encodePassword(request.getPassword());
        UserEntity result = userRepository.save(request.toEntity(encryptedPassword));

        UserResponseDto response = UserResponseDto.toResponse(result);

        return ResponseApi.success(HttpStatus.CREATED, response);
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

    private ResponseCookie deleteRefreshTokenCookie() {
        return ResponseCookie
                .from("refreshToken", "")
                .maxAge(0)
                .path("/api")
                .build();
    }
}
