package com.group6.accommodation.domain.likes.service;

import com.group6.accommodation.domain.accommodation.converter.AccommodationConverter;
import com.group6.accommodation.domain.accommodation.model.dto.AccommodationResponseDto;
import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.accommodation.repository.AccommodationRepository;
import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import com.group6.accommodation.domain.auth.repository.UserRepository;
import com.group6.accommodation.domain.likes.model.dto.UserLikeResponseDto;
import com.group6.accommodation.domain.likes.model.entity.UserLikeEntity;
import com.group6.accommodation.domain.likes.model.entity.UserLikeId;
import com.group6.accommodation.domain.likes.repository.UserLikeRepository;
import com.group6.accommodation.global.exception.error.UserLikeErrorCode;
import com.group6.accommodation.global.exception.type.UserLikeException;
import com.group6.accommodation.global.model.dto.PagedDto;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserLikeService {

    private final UserLikeRepository userLikeRepository;
    private final AccommodationRepository accommodationRepository;
    private final AccommodationConverter accommodationConverter;
    private final UserRepository userRepository;

    @Transactional
    public UserLikeResponseDto addLike(Long accommodationId, Long userId) {
        AccommodationEntity accommodationEntity = getAccommodationById(accommodationId);
        UserEntity authEntity = getUserById(userId);
        checkIfAlreadyLiked(accommodationId, userId);

        UserLikeId userLikeId = new UserLikeId(userId, accommodationId);
        UserLikeEntity addUserLike = UserLikeEntity.builder()
            .id(userLikeId)
            .accommodation(accommodationEntity)
            .user(authEntity)
            .build();

        userLikeRepository.save(addUserLike);
        accommodationRepository.incrementLikeCount(accommodationId);

        return UserLikeResponseDto.toDto(addUserLike);
    }

    @Transactional
    public String cancelLike(Long accommodationId, long userId) {
        getAccommodationById(accommodationId);
        getUserById(userId);

        Optional<UserLikeEntity> isExistUserLike = userLikeRepository.findByAccommodationIdAndUserId(accommodationId, userId);

        if (isExistUserLike.isPresent()) {
            userLikeRepository.delete(isExistUserLike.get());
            accommodationRepository.decrementLikeCount(accommodationId);
            return "Delete Success";
        }
        throw new UserLikeException(UserLikeErrorCode.ACCOMMODATION_NOT_LIKED);
    }

    public PagedDto<AccommodationResponseDto> getLikedAccommodation(Long userId, int page, int size) {
        getUserById(userId);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

        List<UserLikeEntity> userLikes = userLikeRepository.findByUserId(userId);
        List<Long> accommodationIds = userLikes.stream()
            .map(userLike -> userLike.getAccommodation().getId())
            .collect(Collectors.toList());

        Page<AccommodationEntity> accommodationPage = accommodationRepository.findByIdIn(accommodationIds, pageRequest);
        List<AccommodationResponseDto> accommodationDtoList = accommodationConverter.toDtoList(accommodationPage.getContent());

        return new PagedDto<>(
            (int) accommodationPage.getTotalElements(),
            accommodationPage.getTotalPages(),
            accommodationPage.getSize(),
            accommodationPage.getNumber(),
            accommodationDtoList
        );
    }

    private AccommodationEntity getAccommodationById(Long accommodationId) {
        return accommodationRepository.findById(accommodationId)
            .orElseThrow(() -> new UserLikeException(UserLikeErrorCode.ACCOMMODATION_NOT_EXIST));
    }

    private UserEntity getUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserLikeException(UserLikeErrorCode.UNAUTHORIZED));
    }

    private void checkIfAlreadyLiked(Long accommodationId, Long userId) {
        Optional<UserLikeEntity> isExistUserLike = userLikeRepository.findByAccommodationIdAndUserId(accommodationId, userId);
        if (isExistUserLike.isPresent()) {
            throw new UserLikeException(UserLikeErrorCode.ALREADY_ADD_LIKE);
        }
    }
}