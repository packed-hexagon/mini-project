package com.group6.accommodation.domain.auth.repository;

import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import java.util.Optional;

import com.group6.accommodation.global.exception.error.AuthErrorCode;
import com.group6.accommodation.global.exception.type.AuthException;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<UserEntity> findByEmail(String email);

    @NonNull
    default UserEntity getById(@NonNull Long id) {
        return findById(id).orElseThrow(
                () -> new AuthException(AuthErrorCode.NOT_FOUND_USER_BY_USER_ID)
        );
    }
}
