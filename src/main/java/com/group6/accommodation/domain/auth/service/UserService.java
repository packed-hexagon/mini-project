package com.group6.accommodation.domain.auth.service;

import com.group6.accommodation.domain.auth.model.dto.UserRequestDto;
import com.group6.accommodation.domain.auth.model.dto.UserResponseDto;
import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import com.group6.accommodation.domain.auth.repository.UserRepository;
import com.group6.accommodation.global.exception.error.AuthErrorCode;
import com.group6.accommodation.global.exception.type.AuthException;
import com.group6.accommodation.global.redis.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.group6.accommodation.global.security.filter.JwtFilter;
import com.group6.accommodation.global.security.token.model.dto.LoginTokenResponseDto;
import com.group6.accommodation.global.security.token.provider.TokenProvider;
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

    public void logout(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new AuthException(AuthErrorCode.NOT_FOUNT_USER_BY_USER_ID);
        }

        if (refreshTokenRepository.existsById(userId)) {
            refreshTokenRepository.deleteById(userId);
        } else {
            throw new AuthException(AuthErrorCode.ALREADY_LOGOUT);
        }
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public LoginTokenResponseDto refreshTokens(String bearerAccessToken, String refreshToken) {
        String accessToken = resolveBearerAccessToken(bearerAccessToken);
        if (tokenProvider.isTokenExpired(accessToken)) {
            return tokenProvider.getRefreshTokens(refreshToken);
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

}
