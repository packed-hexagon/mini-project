package com.group6.accommodation.global.security.filter;

import com.group6.accommodation.global.exception.error.AuthErrorCode;
import com.group6.accommodation.global.exception.type.AuthException;
import com.group6.accommodation.global.security.token.provider.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String excludePath = "/open-api";
        String path = request.getRequestURI();
        return path.startsWith(excludePath);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = resolveBearerToken(request);

        if (StringUtils.hasText(accessToken) && tokenProvider.validateTokenClaim(accessToken)) {
            Authentication authentication = tokenProvider.getAuthentication(accessToken);
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
            throw new AuthException(AuthErrorCode.UNKNOWN_AUTH_ERROR);
        }
    }
}
