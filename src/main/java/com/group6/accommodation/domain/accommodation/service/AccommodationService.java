package com.group6.accommodation.domain.accommodation.service;

import com.group6.accommodation.domain.accommodation.converter.AccommodationConverter;
import com.group6.accommodation.domain.accommodation.model.dto.AccommodationDetailResponseDto;
import com.group6.accommodation.domain.accommodation.model.dto.AccommodationResponseDto;
import com.group6.accommodation.domain.accommodation.specification.AccommodationSpecification;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.group6.accommodation.domain.accommodation.specification.AccommodationSpecification.*;

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
        int customSize = 12;
        if(areaCode == null && categoryCode == null) {
            responsePagedDto = findAllPage(page, customSize);
        } else if(areaCode == null && categoryCode != null) {
            responsePagedDto = findByCategoryPaged(categoryCode, page, customSize);
        } else if(areaCode != null && categoryCode == null) {
            responsePagedDto = findByAreaPaged(areaCode, page, 9);
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

    // 조건에 부합하는 숙소 조회
    public PagedDto<AccommodationResponseDto> findAvaliableAccommodation(String area, LocalDate startDate, LocalDate endDate, Integer headcount, int page) {
        Specification<AccommodationEntity> spec = Specification.where(AccommodationSpecification.withDistinctAndGroupBy());

        // 위치 조건이 있을 경우
        if (area != null && !area.isEmpty()) {
            String areaCode = Area.getCodeByName(area);
            spec = spec.and(AccommodationSpecification.withArea(areaCode));
        }

        // 날짜 범위 조건 있을 경우
        if(startDate != null && endDate != null) {
            // 인원수 조건도 있을 경우
            if(headcount != null && headcount > 0) {
                spec = spec.and(AccommodationSpecification.withDateRangeAndHeadcount(startDate, endDate, headcount));
            }
            // 인원수 조건 없을 경우
            else {
                spec = spec.and(AccommodationSpecification.withDateRange(startDate, endDate));
            }
        }
        // 날짜 범위 조건 없고 인원수 조건만 있을 경우
        else if(headcount != null && headcount > 0) {
            spec = spec.and(AccommodationSpecification.withHeadcount(headcount));
        }

        spec = spec.and(AccommodationSpecification.withAvailableRooms());

        List<AccommodationEntity> allAccommodation = accommodationRepository.findAll(spec);

        PageRequest pageRequest = PageRequest.of(page, 9, Sort.by(Sort.Direction.DESC, "likeCount"));
        Page<AccommodationEntity> accommodationPage = accommodationRepository.findAllWithCountQuery(allAccommodation, pageRequest);

        return getPagedDto(accommodationPage);
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
