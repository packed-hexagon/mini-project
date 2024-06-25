package com.group6.accommodation.global.security.token.provider;

import com.group6.accommodation.global.exception.error.AuthErrorCode;
import com.group6.accommodation.global.exception.type.AuthException;
import com.group6.accommodation.global.security.service.CustomUserDetails;
import com.group6.accommodation.global.security.token.model.dto.LoginTokenResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
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
    private final int tokenExpireTime;

    public TokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration-time}") int tokenExpireTime) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.tokenExpireTime = tokenExpireTime;
    }

    public LoginTokenResponseDto createToken(Authentication authentication) {
        String authorities = getAuthorities(authentication);
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        return LoginTokenResponseDto.builder()
                .accessToken(createAccessToken(authorities, customUserDetails))
                .refreshToken(createRefreshToken(authorities, customUserDetails))
                .build();
    }

    private String getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElseThrow(() -> new NoSuchElementException("Not Found authorities"));
    }

    private String createAccessToken(String authorities, CustomUserDetails customUserDetails) {
        long now = (new Date()).getTime();
        Date expiredAt = new Date(now + this.tokenExpireTime);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", customUserDetails.getUserId());
        claims.put("role", authorities);
        return Jwts.builder()
                .signWith(key)
                .claims(claims)
                .expiration(expiredAt)
                .compact();
    }

    private String createRefreshToken(String authorities, CustomUserDetails customUserDetails) {
        long now = (new Date()).getTime();
        Date expiredAt = new Date(now + this.tokenExpireTime * 7L);

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

    public boolean validateTokenClaim(String token) {
        try {
            tokenParser(token);
            return true;
        } catch (Exception e) {
            if (e instanceof SignatureException) {
                throw new AuthException(AuthErrorCode.INVALID_TOKEN);
            } else if (e instanceof ExpiredJwtException) {
                throw new AuthException(AuthErrorCode.EXPIRED_TOKEN);
            } else {
                throw new AuthException(AuthErrorCode.UNKNOWN_AUTH_ERROR);
            }
        }
    }
}
