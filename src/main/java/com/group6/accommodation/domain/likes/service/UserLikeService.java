package com.group6.accommodation.domain.likes.service;

import com.group6.accommodation.domain.likes.model.dto.UserLikeDto;
import com.group6.accommodation.domain.likes.model.entity.UserLikeEntity;
import com.group6.accommodation.domain.likes.repository.UserLikeRepository;
import com.group6.accommodation.global.exception.error.ExampleErrorCode;
import com.group6.accommodation.global.exception.type.ExampleException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserLikeService {

    private final UserLikeRepository userLikeRepository;
    private final AccommodationRepository accommodationRepository;
    private final AuthRepository authRepository;

    @Transactional
    public UserLikeDto addLikes(
        Long accommodationId, Long userId
    ) {
        // TODO : accommodation과 합치고 다시 수정
        var accommodationEntity = accommodationRepository.findById(accommodationId)
            .orElseThrow(() -> new ExampleException(ExampleErrorCode.TEST));

        var authEntity = authRepository.findById(userId)
            .orElseThrow(() -> new ExampleException(ExampleErrorCode.TEST));

        Optional<UserLikeEntity> isExistUserLike = userLikeRepository.findByAccommodationId(accommodationId);

        // TODO : 실패 reponse 수정 필요
        if (isExistUserLike.isPresent()) {
            UserLikeEntity userLikeEntity = isExistUserLike.get();
            throw new ExampleException(ExampleErrorCode.TEST);
        } else {
            UserLikeEntity addUserLike = UserLikeEntity.builder()
                .accommodation(accommodationEntity)
                .user(authEntity)
                .build()
                ;
            addUserLike = userLikeRepository.save(addUserLike);
            accommodationRepository.incrementLikeCount(accommodationId);
            return UserLikeDto.toDto(addUserLike);
        }
    }
}
