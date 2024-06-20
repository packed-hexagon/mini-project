package com.group6.accommodation.domain.auth.repository;

import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
