package com.group6.accommodation.domain.auth.repository;

import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<UserEntity> findByEmail(String email);
}
