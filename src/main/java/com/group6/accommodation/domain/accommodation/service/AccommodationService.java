package com.group6.accommodation.domain.accommodation.service;

import com.group6.accommodation.domain.accommodation.converter.AccommodationConverter;
import com.group6.accommodation.domain.accommodation.model.dto.AccommodationDetailResponseDto;
import com.group6.accommodation.domain.accommodation.model.dto.AccommodationResponseDto;
import com.group6.accommodation.global.model.dto.PagedDto;
import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.accommodation.model.enums.Area;
import com.group6.accommodation.domain.accommodation.model.enums.Category;
import com.group6.accommodation.domain.accommodation.repository.AccommodationRepository;
import com.group6.accommodation.global.exception.error.AccommodationErrorCode;
import com.group6.accommodation.global.exception.type.AccommodationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;
    private final AccommodationConverter accommodationConverter;

    // Open api에서 불러온 데이터 저장
    public void saveAccommodations(List<AccommodationEntity> accommodations) {
        for (AccommodationEntity accommodation : accommodations) {
            Optional<AccommodationEntity> existingAccommodation = accommodationRepository.findById(accommodation.getId());
            if (existingAccommodation.isPresent()) {
                // 업데이트 로직
                AccommodationEntity existing = existingAccommodation.get();
                AccommodationEntity updated = AccommodationEntity.builder()
                        .id(existing.getId()) // ID는 기존 엔티티의 ID를 유지
                        .title(accommodation.getTitle())
                        .address(accommodation.getAddress())
                        .address2(accommodation.getAddress2())
                        .areacode(accommodation.getAreacode())
                        .sigungucode(accommodation.getSigungucode())
                        .category(accommodation.getCategory())
                        .image(accommodation.getImage())
                        .thumbnail(accommodation.getThumbnail())
                        .latitude(accommodation.getLatitude())
                        .longitude(accommodation.getLongitude())
                        .mlevel(accommodation.getMlevel())
                        .tel(accommodation.getTel())
                        .likeCount(accommodation.getLikeCount())
                        .rating(existing.getRating()) // 필요에 따라 기존 엔티티의 다른 필드도 유지
                        .build();
                accommodationRepository.save(updated);
            } else {
                // 새로운 데이터 추가
                accommodationRepository.save(accommodation);
            }
        }
    }

    // 매개변수에 따라 숙소 조회
    public PagedDto<AccommodationResponseDto> findByParameter(String areaCode, String categoryCode, int page) {
        PagedDto<AccommodationResponseDto> responsePagedDto = new PagedDto<>();
        int customSize = 9;
        if(areaCode == null && categoryCode == null) {
            responsePagedDto = findAllPage(page, 12);
        } else if(areaCode == null && categoryCode != null) {
            responsePagedDto = findByCategoryPaged(categoryCode, page, customSize);
        } else if(areaCode != null && categoryCode == null) {
            responsePagedDto = findByAreaPaged(areaCode, page, customSize);
        }

        return responsePagedDto;
    }


    // 숙소 전체 조회
    public PagedDto<AccommodationResponseDto> findAllPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "likeCount"));
        Page<AccommodationEntity> accommodationPage = accommodationRepository.findAll(pageRequest);

        return getPagedDto(accommodationPage);
    }

    // 숙소 종류별 조회
    public PagedDto<AccommodationResponseDto> findByCategoryPaged(String category, int page, int size) {
        String categoryCode = Category.getCodeByName(category);
        Page<AccommodationEntity> accommodationPage = accommodationRepository.findByCategory(categoryCode, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "likeCount")));

        return getPagedDto(accommodationPage);
    }

    // 숙소 지역별 조회
    public PagedDto<AccommodationResponseDto> findByAreaPaged(String area, int page, int size) {
        String areaCode = Area.getCodeByName(area);
        Page<AccommodationEntity> accommodationPage = accommodationRepository.findByAreacode(areaCode, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "likeCount")));

        return getPagedDto(accommodationPage);
    }

    // 숙소 단건 조회
    public AccommodationDetailResponseDto findById(Long id) {
        AccommodationEntity accommodation = accommodationRepository.findById(id)
                .orElseThrow(() -> new AccommodationException(
                        AccommodationErrorCode.NOT_FOUND_ACCOMMODATION));

        AccommodationDetailResponseDto accommodationDetailResponseDto = accommodationConverter.toDetailDto(accommodation);
        return accommodationDetailResponseDto;
    }

    // 키워드로 숙소 조회
    public PagedDto<AccommodationResponseDto> findByKeywordPaged(String keyword, int page) {
        int customSize = 9;
        Page<AccommodationEntity> accommodationPage = accommodationRepository.findByTitleOrAddressContainingKeyword(keyword, PageRequest.of(page, customSize, Sort.by(Sort.Direction.DESC, "likeCount")));

        if (accommodationPage.isEmpty()) {
            throw new AccommodationException(AccommodationErrorCode.NOT_FOUND_KEYWORD_ACCOMMODATION);
        } else {
            return getPagedDto(accommodationPage);
        }
    }

    // Page 정보값 포함한 PagedDto로 변환.(공통 로직)
    public PagedDto getPagedDto(Page<AccommodationEntity> entity) {
        List<AccommodationResponseDto> accommodationList = accommodationConverter.toDtoList(entity.getContent());

        PagedDto pagedDto = new PagedDto<>(
                (int) entity.getTotalElements(),
                entity.getTotalPages(),
                entity.getSize(),
                entity.getNumber(),
                accommodationList
        );
        return pagedDto;
    }
}
