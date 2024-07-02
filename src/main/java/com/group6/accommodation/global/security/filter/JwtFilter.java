package com.group6.accommodation.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group6.accommodation.global.exception.error.AuthErrorCode;
import com.group6.accommodation.global.exception.type.AuthException;
import com.group6.accommodation.global.security.token.model.dto.LoginTokenResponseDto;
import com.group6.accommodation.global.security.token.provider.TokenProvider;
import com.group6.accommodation.global.util.ResponseApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String excludePath = "/open-api";
        String path = request.getRequestURI();
        return path.startsWith(excludePath);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = resolveBearerToken(request);

        if (tokenProvider.isTokenExpired(token)) {
            // 쿠키에서 받아오기
            String refreshTokenFromCookie = tokenProvider.getRefreshTokenFromCookies(request);
            // 쿠키 토큰과 redis 토큰과 비교해서 refresh 만료 안 됐으면 새로운 access, refresh 발급
            LoginTokenResponseDto refreshToken = tokenProvider.getRefreshToken(refreshTokenFromCookie);

            Authentication authentication = tokenProvider.getAuthentication(refreshToken.getAccessToken());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            ResponseApi<Map<String, String>> responseBody = ResponseApi.success(HttpStatus.OK, Map.of(
                    "accessToken", refreshToken.getAccessToken(),
                    "refreshToken", refreshToken.getRefreshToken()
            ));

            Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken.getRefreshToken());
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(true);
            refreshTokenCookie.setPath("/api");
            refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);

            response.setCharacterEncoding("UTF-8");
            response.addCookie(refreshTokenCookie);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().write(objectMapper.writeValueAsString(responseBody));

            return;
        }

        if (StringUtils.hasText(token) && tokenProvider.validateTokenClaim(token)) {
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        try {
            if (!StringUtils.hasText(bearerToken)) {
                throw new AuthException(AuthErrorCode.EMPTY_TOKEN);
            }
            if (!bearerToken.startsWith(TOKEN_PREFIX)) {
                throw new AuthException(AuthErrorCode.UNSUPPORTED_TOKEN_TYPE);
            }
            return bearerToken.substring(TOKEN_PREFIX.length());
        } catch (AuthException e) {
            log.error("토큰 검증 실패 : {}", e.getMessage());
        }
//        throw new AuthException(AuthErrorCode.UNKNOWN_AUTH_ERROR);
        return null;
    }
}
