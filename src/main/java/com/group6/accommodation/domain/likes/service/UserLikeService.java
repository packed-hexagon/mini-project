package com.group6.accommodation.domain.likes.service;

import com.group6.accommodation.domain.accommodation.converter.AccommodationConverter;
import com.group6.accommodation.domain.accommodation.model.dto.AccommodationResponseDto;
import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.accommodation.repository.AccommodationRepository;
import com.group6.accommodation.domain.auth.repository.UserRepository;
import com.group6.accommodation.domain.likes.model.dto.UserLikeResponseDto;
import com.group6.accommodation.domain.likes.model.entity.UserLikeEntity;
import com.group6.accommodation.domain.likes.model.entity.UserLikeId;
import com.group6.accommodation.domain.likes.repository.UserLikeRepository;
import com.group6.accommodation.global.exception.error.UserLikeErrorCode;
import com.group6.accommodation.global.exception.type.UserLikeException;
import com.group6.accommodation.global.model.dto.PagedDto;
import com.group6.accommodation.global.util.ResponseApi;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
    public ResponseApi<UserLikeResponseDto> addLike(
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
            userLikeRepository.save(addUserLike);
            accommodationRepository.incrementLikeCount(accommodationId);

            var result =  UserLikeResponseDto.toDto(addUserLike);
            return ResponseApi.success(HttpStatus.CREATED, result);
        }
    }

    @Transactional
    public ResponseApi<String> cancelLike(
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
            accommodationRepository.decrementLikeCount(accommodationId);
            return ResponseApi.success(HttpStatus.NO_CONTENT, "Delete Success");
        } else {
            throw new UserLikeException(UserLikeErrorCode.ACCOMMODATION_NOT_LIKED);
        }
    }

    public ResponseApi<PagedDto<AccommodationResponseDto>> getLikedAccommodation(
        Long userId, int page, int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

        // 로그인한 객체 가져오기
        userRepository.findById(userId)
            .orElseThrow(() -> new UserLikeException(UserLikeErrorCode.UNAUTHORIZED));

        // 사용자 찜 목록
        List<UserLikeEntity> userLikes = userLikeRepository.findByUserId(userId);

        // 찜한 숙박 정보 목록
        List<Long> accommodationIds = userLikes.stream()
            .map(userLike -> userLike.getAccommodation().getId())
            .collect(Collectors.toList());

        Page<AccommodationEntity> accommodationPage = accommodationRepository.findByIdIn(accommodationIds, pageRequest);

        List<AccommodationResponseDto> accommodationDtoList = accommodationConverter.toDtoList(accommodationPage.getContent());

        PagedDto pagedDto = new PagedDto<>(
            (int) accommodationPage.getTotalElements(),
            accommodationPage.getTotalPages(),
            accommodationPage.getSize(),
            accommodationPage.getNumber(),
            accommodationDtoList
        );

        return ResponseApi.success(HttpStatus.OK, pagedDto);
    }
}
