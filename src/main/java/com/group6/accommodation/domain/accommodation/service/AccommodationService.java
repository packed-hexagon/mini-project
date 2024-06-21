package com.group6.accommodation.domain.accommodation.service;

import com.group6.accommodation.domain.accommodation.converter.AccommodationConverter;
import com.group6.accommodation.domain.accommodation.model.dto.AccommodationDetailDto;
import com.group6.accommodation.domain.accommodation.model.dto.AccommodationDto;
import com.group6.accommodation.domain.accommodation.model.dto.PagedDto;
import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.accommodation.model.enums.Area;
import com.group6.accommodation.domain.accommodation.model.enums.Category;
import com.group6.accommodation.domain.accommodation.repository.AccommodationRepository;
import com.group6.accommodation.domain.likes.repository.UserLikeRepository;
import com.group6.accommodation.global.exception.error.AccommodationErrorCode;
import com.group6.accommodation.global.exception.type.AccommodationException;
import com.group6.accommodation.global.util.ResponseApi;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;
    private final AccommodationConverter accommodationConverter;
    private final UserLikeRepository userLikeRepository;

    // 숙소 전체 조회
    public ResponseApi<PagedDto<AccommodationDto>> findAllPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "likeCount"));
        Page<AccommodationEntity> accommodationPage = accommodationRepository.findAll(pageRequest);
        List<AccommodationDto> accommodationDtoList = accommodationConverter.toDtoList(accommodationPage.getContent());

        if(page >= accommodationPage.getTotalPages()) {
            throw new AccommodationException(AccommodationErrorCode.NOT_FOUND_DATA_PAGE);
        }

        PagedDto pagedDto = new PagedDto<>(
                (int) accommodationPage.getTotalElements(),
                accommodationPage.getTotalPages(),
                accommodationPage.getSize(),
                accommodationPage.getNumber(),
                accommodationDtoList
        );

        return ResponseApi.success(HttpStatus.OK, pagedDto);
    }

    // 숙소 종류별 조회
    public ResponseApi<PagedDto<AccommodationDto>> findByCategoryPaged(String category, int page, int size) {
        String categoryCode = Category.getCodeByName(category);
        Page<AccommodationEntity> accommodationPage = accommodationRepository.findByCategory(categoryCode, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "likeCount")));
        List<AccommodationDto> accommodationDtoList = accommodationConverter.toDtoList(accommodationPage.getContent());

        if(page >= accommodationPage.getTotalPages()) {
            throw new AccommodationException(AccommodationErrorCode.NOT_FOUND_DATA_PAGE);
        }
        PagedDto pagedDto = new PagedDto<>(
                (int) accommodationPage.getTotalElements(),
                accommodationPage.getTotalPages(),
                accommodationPage.getSize(),
                accommodationPage.getNumber(),
                accommodationDtoList
        );

        return ResponseApi.success(HttpStatus.OK, pagedDto);
    }

    // 숙소 지역별 조회
    public ResponseApi<PagedDto<AccommodationDto>> findByAreaPaged(String area, int page, int size) {
        String areaCode = Area.getCodeByName(area);
        Page<AccommodationEntity> accommodationPage = accommodationRepository.findByAreacode(areaCode, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "likeCount")));
        List<AccommodationDto> accommodationDtoList = accommodationConverter.toDtoList(accommodationPage.getContent());

        if(page >= accommodationPage.getTotalPages()) {
            throw new AccommodationException(AccommodationErrorCode.NOT_FOUND_DATA_PAGE);
        }

        PagedDto pagedDto = new PagedDto<>(
                (int) accommodationPage.getTotalElements(),
                accommodationPage.getTotalPages(),
                accommodationPage.getSize(),
                accommodationPage.getNumber(),
                accommodationDtoList
        );

        return ResponseApi.success(HttpStatus.OK, pagedDto);
    }

    // 숙소 단건 조회
    public ResponseApi<AccommodationDetailDto> findById(Long id) {
        AccommodationEntity accommodation = accommodationRepository.findById(id)
                .orElseThrow(() -> new AccommodationException(
                        AccommodationErrorCode.NOT_FOUND_ACCOMMODATION));

        AccommodationDetailDto accommodationDetailDto = accommodationConverter.toDetailDto(accommodation);
        return ResponseApi.success(HttpStatus.OK, accommodationDetailDto);
    }

    // 숙소 찜 개수 업데이트
    public void updateLike(Long id) {
        AccommodationEntity accommodation = accommodationRepository.findById(id).get();
//        accommodation.setLikeCount(userLikeRepository.countByAccommodationId(accommodationId));
//        accommodationRepository.save(accommodation);
    }

}
