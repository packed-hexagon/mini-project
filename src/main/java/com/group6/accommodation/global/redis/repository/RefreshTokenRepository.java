package com.group6.accommodation.global.redis.repository;

import com.group6.accommodation.global.redis.model.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
}
