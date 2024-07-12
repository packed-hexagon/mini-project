package com.group6.accommodation.domain.auth.service;

import com.group6.accommodation.domain.auth.model.dto.UserRequestDto;
import com.group6.accommodation.domain.auth.model.dto.UserResponseDto;
import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import com.group6.accommodation.domain.auth.repository.UserRepository;
import com.group6.accommodation.global.exception.error.AuthErrorCode;
import com.group6.accommodation.global.exception.type.AuthException;
import com.group6.accommodation.global.redis.repository.RefreshTokenRepository;
import com.group6.accommodation.global.security.token.model.dto.LoginTokenResponseDto;
import com.group6.accommodation.global.security.token.provider.TokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenProvider tokenProvider;

    private UserEntity user;
    private UserRequestDto userRequestDto;
    private LoginTokenResponseDto refreshTokens;

    @BeforeEach
    public void setup() {
        user = UserEntity.builder()
                .id(1L)
                .email("test@example.com")
                .encryptedPassword("encryptedPassword")
                .phoneNumber("010-1234-2345")
                .name("testName")
                .build();

        userRequestDto = UserRequestDto.builder()
                .email("test@example.com")
                .password("password")
                .phoneNumber("010-1234-2345")
                .name("testName")
                .build();

        refreshTokens = LoginTokenResponseDto.builder()
                .accessToken("newAccessToken")
                .refreshToken("newRefreshToken")
                .build();
    }

    @Test
    @DisplayName("사용자 정보 조회 성공")
    public void getExistsUserInfoTest() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserResponseDto result = userService.getUserInfo(user.getId());

        assertNotNull(result);
        assertEquals(user.getId(), result.getUserId());
    }

    @Test
    @DisplayName("사용자 정보 조회 실패 - 존재하지 않는 사용자")
    public void getNotFoundUserInfoTest() {
        Long notExistsUserId = 2L;
        when(userRepository.findById(notExistsUserId)).thenReturn(Optional.empty());

        AuthException exception = assertThrows(AuthException.class,
                () -> userService.getUserInfo(2L));

        assertEquals(AuthErrorCode.NOT_FOUNT_USER_BY_USER_ID.getInfo(), exception.getInfo());
    }

    @Test
    @DisplayName("회원가입 성공")
    public void registerSuccessTest() {
        when(userRepository.existsByEmail(userRequestDto.getEmail())).thenReturn(false);
        when(userRepository.existsByPhoneNumber(userRequestDto.getPhoneNumber())).thenReturn(false);
        when(passwordEncoder.encode(userRequestDto.getPassword())).thenReturn("encryptedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        UserResponseDto result = userService.register(userRequestDto);

        assertNotNull(result);
        assertEquals(user.getId(), result.getUserId());
    }

    @Test
    @DisplayName("회원가입 실패 - 이미 존재하는 사용자")
    public void ExistEmailRegisterTest() {
        when(userRepository.existsByEmail(userRequestDto.getEmail())).thenReturn(true);

        AuthException exception = assertThrows(AuthException.class,
                () -> userService.register(userRequestDto));

        assertEquals(AuthErrorCode.ALREADY_EXIST_EMAIL.getInfo(), exception.getInfo());
    }

    @Test
    @DisplayName("회원가입 실패 - 이미 존재하는 휴대폰 번호")
    public void ExistPhoneNumberRegisterTest() {
        when(userRepository.existsByPhoneNumber(userRequestDto.getPhoneNumber())).thenReturn(true);

        AuthException exception = assertThrows(AuthException.class,
                () -> userService.register(userRequestDto));

        assertEquals(AuthErrorCode.ALREADY_EXIST_PHONE_NUMBER.getInfo(), exception.getInfo());
    }

    @Test
    @DisplayName("로그아웃 성공")
    public void logoutSuccessTest() {
        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(refreshTokenRepository.existsById(user.getId())).thenReturn(true);

        HttpHeaders headers = userService.logout(user.getId());

        verify(refreshTokenRepository, times(1)).deleteById(user.getId());
        assertNotNull(headers.get(HttpHeaders.SET_COOKIE));
    }

    @Test
    @DisplayName("로그아웃 실패 - 존재하지 않는 사용자")
    public void notFoundUserTest() {
        when(userRepository.existsById(user.getId())).thenReturn(false);

        AuthException exception = assertThrows(AuthException.class,
                () -> userService.logout(user.getId()));

        assertEquals(AuthErrorCode.NOT_FOUNT_USER_BY_USER_ID.getInfo(), exception.getInfo());
    }

    @Test
    @DisplayName("로그아웃 실패 - 이미 로그아웃 한 사용자")
    public void alreadyLogoutUserTest() {
        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(refreshTokenRepository.existsById(user.getId())).thenReturn(false);

        AuthException exception = assertThrows(AuthException.class,
                () -> userService.logout(user.getId()));

        assertEquals(AuthErrorCode.ALREADY_LOGOUT.getInfo(), exception.getInfo());
    }

    @Test
    @DisplayName("토큰 갱신 성공")
    public void refreshTokensSuccessTest() {
        String bearerAccessToken = "Bearer accessToken";
        String refreshToken = "refreshToken";

        when(tokenProvider.isTokenExpired(anyString())).thenReturn(true);
        when(tokenProvider.getRefreshTokens(anyString())).thenReturn(refreshTokens);

        LoginTokenResponseDto result = userService.refreshTokens(bearerAccessToken, refreshToken);

        assertNotNull(result);
        assertEquals(result.getAccessToken(), refreshTokens.getAccessToken());
        assertEquals(result.getRefreshToken(), refreshTokens.getRefreshToken());
    }

    @Test
    @DisplayName("토큰 갱신 실패 - 아직 유효한 AccessToken")
    public void accessTokenNotExpiredRefreshTokensTest() {

        String bearerAccessToken = "Bearer accessToken";
        String refreshToken = "refreshToken";

        when(tokenProvider.isTokenExpired(anyString())).thenReturn(false);

        AuthException exception = assertThrows(AuthException.class,
                () -> userService.refreshTokens(bearerAccessToken, refreshToken));

        assertEquals(exception.getInfo(), AuthErrorCode.INVALID_TOKEN.getInfo());
    }
}
