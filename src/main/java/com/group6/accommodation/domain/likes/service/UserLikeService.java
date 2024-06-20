package com.group6.accommodation.domain.likes.service;

import com.group6.accommodation.domain.accommodation.repository.AccommodationRepository;
import com.group6.accommodation.domain.auth.repository.UserRepository;
import com.group6.accommodation.domain.likes.model.dto.UserLikeResponseDto;
import com.group6.accommodation.domain.likes.model.entity.UserLikeEntity;
import com.group6.accommodation.domain.likes.model.entity.UserLikeId;
import com.group6.accommodation.domain.likes.repository.UserLikeRepository;
import com.group6.accommodation.global.exception.error.UserLikeErrorCode;
import com.group6.accommodation.global.exception.type.UserLikeException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserLikeService {

    private final UserLikeRepository userLikeRepository;
    private final AccommodationRepository accommodationRepository;
    private final UserRepository userRepository;

    @Transactional
    public UserLikeResponseDto addLike(
        Long accommodationId, Long userId
    ) {
        // 해당 숙박 정보가 있는지 확인
        var accommodationEntity = accommodationRepository.findById(accommodationId)
            .orElseThrow(() -> new UserLikeException(UserLikeErrorCode.ACCOMMODATION_NOT_EXIST));

        // 로그인한 객체 가져오기
        var authEntity = userRepository.findById(userId)
            .orElseThrow(() -> new UserLikeException(UserLikeErrorCode.UNAUTHORIZED));

        // userLikeId 생성
        UserLikeId userLikeId = new UserLikeId();
        userLikeId.setUserId(userId);
        userLikeId.setAccommodationId(accommodationId);

        // 이미 찜했는지 여부 확인
        Optional<UserLikeEntity> isExistUserLike = userLikeRepository.findByAccommodationIdAndUserId(accommodationId, userId);

        if (isExistUserLike.isPresent()) {
            throw new UserLikeException(UserLikeErrorCode.ALREADY_ADD_LIKE);
        } else {
            UserLikeEntity addUserLike = UserLikeEntity.builder()
                .id(userLikeId)
                .accommodation(accommodationEntity)
                .user(authEntity)
                .build()
                ;
            addUserLike = userLikeRepository.save(addUserLike);
//            accommodationRepository.incrementLikeCount(accommodationId);
            return UserLikeResponseDto.toDto(addUserLike);
        }
    }

    @Transactional
    public String cancelLike(
        Long accommodationId, long userId
    ) {
        // 해당 숙박 정보가 있는지 확인
        accommodationRepository.findById(accommodationId)
            .orElseThrow(() -> new UserLikeException(UserLikeErrorCode.ACCOMMODATION_NOT_EXIST));

        // 로그인한 객체 가져오기
        userRepository.findById(userId)
            .orElseThrow(() -> new UserLikeException(UserLikeErrorCode.UNAUTHORIZED));

        // 이미 찜했는지 여부 확인
        Optional<UserLikeEntity> isExistUserLike = userLikeRepository.findByAccommodationIdAndUserId(accommodationId, userId);

        if (isExistUserLike.isPresent()) {
            userLikeRepository.delete(isExistUserLike.get());
            return "Delete Success";
        } else {
            throw new UserLikeException(UserLikeErrorCode.ACCOMMODATION_NOT_LIKED);
        }
    }
}
