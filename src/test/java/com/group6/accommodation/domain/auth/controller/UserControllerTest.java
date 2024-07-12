package com.group6.accommodation.domain.auth.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group6.accommodation.domain.auth.mock.WithMockCustomUser;
import com.group6.accommodation.domain.auth.model.dto.UserRequestDto;
import com.group6.accommodation.domain.auth.model.dto.UserResponseDto;
import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import com.group6.accommodation.domain.auth.repository.UserRepository;
import com.group6.accommodation.domain.auth.service.UserService;
import com.group6.accommodation.global.security.token.model.dto.LoginTokenResponseDto;
import com.group6.accommodation.global.security.token.provider.TokenProvider;
import jakarta.servlet.http.Cookie;
import java.time.Instant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    UserEntity user;
    LoginTokenResponseDto jwtToken;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup( context)
            .apply(springSecurity()).build();

        userRepository.deleteAll();

        user = UserEntity.builder()
            .id(1L)
            .email("abc@test.com")
            .encryptedPassword("{noop}test")
            .name("test")
            .phoneNumber("123-456-7890")
            .build()
            ;

        userRepository.save(user);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getEmail(), "test");
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        jwtToken = tokenProvider.createToken(authentication);
    }

    @AfterEach
    public void tearDown() throws Exception {
        userRepository.deleteAll();
    }

    @Test
    @WithMockCustomUser(email = "abc@test.com", encryptedPassword = "test")
    public void testGetUserInfo() throws Exception {

        UserResponseDto userResponseDto = UserResponseDto.toResponse(user);

        Mockito.when(userService.getUserInfo(any(Long.class))).thenReturn(userResponseDto);

        mockMvc.perform(get("/api/user")
            .header("Authorization", "Bearer " + jwtToken)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.email").value("abc@test.com"))
            .andExpect(jsonPath("$.data.name").value("test"))
            .andExpect(jsonPath("$.data.phoneNumber").value("123-456-7890"))
            ;
    }

    @Test
    public void testRefreshTokens() throws Exception {
        LoginTokenResponseDto loginTokenResponseDto = LoginTokenResponseDto.builder()
            .accessToken(jwtToken.getAccessToken())
            .refreshToken(jwtToken.getRefreshToken())
            .build()
            ;

        Mockito.when(userService.refreshTokens(any(String.class), any(String.class))).thenReturn(loginTokenResponseDto);

        Cookie cookie = new Cookie("refreshToken", jwtToken.getRefreshToken());

        mockMvc.perform(post("/open-api/user/refresh-tokens")
            .header("Authorization", "Bearer " + jwtToken.getAccessToken())
            .cookie(cookie)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.accessToken").value(jwtToken.getAccessToken()))
            .andExpect(jsonPath("$.data.refreshToken").value(jwtToken.getRefreshToken()))
            ;
    }

    @Test
    public void testRegister() throws Exception {
        UserRequestDto userRequestDto = UserRequestDto.builder()
            .email("abc@test.com")
            .password("test")
            .name("test")
            .phoneNumber("010-456-7890")
            .build()
            ;

        UserResponseDto userResponseDto = UserResponseDto.builder()
            .userId(2L)
            .email("abc@test.com")
            .name("test")
            .phoneNumber("010-456-7890")
            .build()
            ;

        Mockito.when(userService.register(any(UserRequestDto.class))).thenReturn(userResponseDto);

        mockMvc.perform(post("/open-api/user/register")
            .content(objectMapper.writeValueAsString(userRequestDto))
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.email").value("abc@test.com"))
            .andExpect(jsonPath("$.data.name").value("test"))
            .andExpect(jsonPath("$.data.phoneNumber").value("010-456-7890"))
            ;
    }

    @Test
    @WithMockCustomUser(email = "abc@test.com", encryptedPassword = "test")
    public void testLogout() throws Exception {
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", "")
            .path("/")
            .maxAge(0)
            .httpOnly(true)
            .build()
            ;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        Mockito.when(userService.logout(any(Long.class))).thenReturn(headers);

        mockMvc.perform(post("/api/user/logout")
            .header("Authorization", "Bearer " + jwtToken)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNoContent())
            .andExpect(header().string("Set-Cookie", containsString("refreshToken=; Path=/; Max-Age=0;")))
            ;
    }
}