package com.group6.accommodation.domain.auth.service;

import com.group6.accommodation.domain.auth.model.dto.UserRegisterRequestDto;
import com.group6.accommodation.domain.auth.model.dto.UserRegisterResponseDto;
import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import com.group6.accommodation.domain.auth.repository.UserRepository;
import com.group6.accommodation.global.exception.error.AuthErrorCode;
import com.group6.accommodation.global.exception.type.AuthException;
import com.group6.accommodation.global.util.ResponseApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseApi<UserRegisterResponseDto> getUserInfo(Long userId) {
        UserEntity result = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException(AuthErrorCode.NOT_FOUNT_USER_BY_USER_ID));

        UserRegisterResponseDto response = UserRegisterResponseDto.toResponse(result);
        return ResponseApi.success(HttpStatus.OK, response);
    }

    public ResponseApi<UserRegisterResponseDto> register(UserRegisterRequestDto request) {
        // 이메일 중복 확인
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthException(AuthErrorCode.ALREADY_EXIST_EMAIL);
        }

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new AuthException(AuthErrorCode.ALREADY_EXIST_PHONE_NUMBER);
        }

        String encryptedPassword = encodePassword(request.getPassword());
        UserEntity result = userRepository.save(request.toEntity(encryptedPassword));

        UserRegisterResponseDto response = UserRegisterResponseDto.toResponse(result);
        log.info("User Registered : {}", result);

        return ResponseApi.success(HttpStatus.CREATED, response);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

}
