package com.group6.accommodation.global.redis.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@AllArgsConstructor
@RedisHash(value = "token")
public class RefreshToken {
    @Id
    private Long userId;

    @Indexed
    private String refreshToken;

    @TimeToLive
    private Long expiration;
}
