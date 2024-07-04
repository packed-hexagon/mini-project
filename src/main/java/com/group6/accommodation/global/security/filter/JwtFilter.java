package com.group6.accommodation.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group6.accommodation.global.exception.error.AuthErrorCode;
import com.group6.accommodation.global.exception.type.AuthException;
import com.group6.accommodation.global.security.token.provider.TokenProvider;
import com.group6.accommodation.global.util.ResponseApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        try {
            String accessToken = resolveBearerToken(request);

            if (StringUtils.hasText(accessToken) && tokenProvider.validateTokenClaim(accessToken)) {
                Authentication authentication = tokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);
        } catch (AuthException e) {
            log.error(e.getMessage(), e);
            response.setStatus(e.getStatusCode().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getWriter(), ResponseApi.failed(e));
        }
    }

    private String resolveBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (!StringUtils.hasText(bearerToken)) {
            throw new AuthException(AuthErrorCode.EMPTY_TOKEN);
        } else if (!bearerToken.startsWith(TOKEN_PREFIX)) {
            throw new AuthException(AuthErrorCode.UNSUPPORTED_TOKEN_TYPE);
        }
        return bearerToken.substring(TOKEN_PREFIX.length());
    }
}
