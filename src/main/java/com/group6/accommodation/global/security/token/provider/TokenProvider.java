package com.group6.accommodation.global.security.token.provider;

import com.group6.accommodation.global.exception.error.AuthErrorCode;
import com.group6.accommodation.global.exception.type.AuthException;
import com.group6.accommodation.global.redis.model.RefreshToken;
import com.group6.accommodation.global.redis.repository.RefreshTokenRepository;
import com.group6.accommodation.global.security.service.CustomUserDetails;
import com.group6.accommodation.global.security.token.model.dto.LoginTokenResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TokenProvider {

    private final SecretKey key;
    private final long accessTokenExpireTime;
    private final long refreshTokenExpireTime;
    private final RefreshTokenRepository refreshTokenRepository;

    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    public TokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-expiration-time}") long accessTokenExpireTime,
            @Value("${jwt.refresh-expiration-time}") long refreshTokenExpireTime,
            RefreshTokenRepository refreshTokenRepository) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.accessTokenExpireTime = accessTokenExpireTime;
        this.refreshTokenExpireTime = refreshTokenExpireTime;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public LoginTokenResponseDto createToken(Authentication authentication) {
        String authorities = getAuthorities(authentication);
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        long now = (new Date()).getTime();

        String accessToken = createAccessToken(authorities, customUserDetails, now);
        String refreshToken = createRefreshToken(authorities, customUserDetails, now);

        RefreshToken tokenEntity = new RefreshToken(customUserDetails.getUserId(), refreshToken,
                now + refreshTokenExpireTime);

        refreshTokenRepository.save(tokenEntity);

        return LoginTokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private String getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElseThrow(() -> new NoSuchElementException("Not Found authorities"));
    }

    private String createAccessToken(String authorities, CustomUserDetails customUserDetails, long now) {
        Date expiredAt = new Date(now + this.accessTokenExpireTime);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", customUserDetails.getUserId());
        claims.put("role", authorities);
        return Jwts.builder()
                .signWith(key)
                .claims(claims)
                .expiration(expiredAt)
                .compact();
    }

    private String createRefreshToken(String authorities, CustomUserDetails customUserDetails, long now) {
        Date expiredAt = new Date(now + this.refreshTokenExpireTime);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", customUserDetails.getUserId());
        claims.put("role", authorities);
        return Jwts.builder()
                .signWith(key)
                .claims(claims)
                .expiration(expiredAt)
                .compact();
    }


    public Authentication getAuthentication(String token) {
        try {
            System.out.println(token);
            Claims claims = tokenParser(token);

            Long userId = claims.get("userId", Long.class);
            Set<SimpleGrantedAuthority> authorities = Collections.singleton(
                    new SimpleGrantedAuthority(claims.get("role", String.class)));
            CustomUserDetails customUserDetails = new CustomUserDetails(userId, "", "");

            return new UsernamePasswordAuthenticationToken(customUserDetails, "", authorities);
        } catch (Exception e) {
            log.error(AuthErrorCode.INVALID_TOKEN.getInfo());
            throw new AuthException(AuthErrorCode.INVALID_TOKEN);
        }
    }

    private Claims tokenParser(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public LoginTokenResponseDto getRefreshToken(String refreshTokenFromCookie) {
        try {
            // 쿠키 토큰 검증 
            Claims claims = tokenParser(refreshTokenFromCookie);
            Long userId = claims.get("userId", Long.class);

            // 레디스에 존재하는지 확인
            RefreshToken refreshTokenEntity = refreshTokenRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("로그아웃 된 refresh Token"));
            
            // access, refresh 새로 생성
            CustomUserDetails customUserDetails = new CustomUserDetails(userId, "", "");
            String authorities = claims.get("role", String.class);

            long now = (new Date()).getTime();
            String newAccessToken = createAccessToken(authorities, customUserDetails, now);
            String newRefreshToken = createRefreshToken(authorities, customUserDetails, now);

            // 레디스에 새로운 refresh token 저장
            RefreshToken newRefreshTokenEntity = new RefreshToken(userId, newRefreshToken, now + refreshTokenExpireTime);
            refreshTokenRepository.save(newRefreshTokenEntity);

            return LoginTokenResponseDto.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .build();

        } catch (Exception e) {
            log.info("Redis에 존재하지 않는 refreshToken");
            throw new RuntimeException(e);
        }
    }

    public boolean validateTokenClaim(String token) {
        try {
            tokenParser(token);
            return true;
        } catch (Exception e) {
            if (e instanceof SignatureException) {
                log.error(AuthErrorCode.INVALID_TOKEN.getInfo());
            } else if (e instanceof ExpiredJwtException) {
                log.error(AuthErrorCode.EXPIRED_TOKEN.getInfo());
            } else {
                log.error(AuthErrorCode.UNKNOWN_AUTH_ERROR.getInfo());
            }
        }
        return false;
    }

    public boolean isTokenExpired(String token) {
        try {
            tokenParser(token);
            return false;
        } catch (ExpiredJwtException e) {
            log.info("토큰 만료");
            return true;
        }
    }

    public String getRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (REFRESH_TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
