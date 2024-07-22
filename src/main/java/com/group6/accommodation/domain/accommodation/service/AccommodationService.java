package com.group6.accommodation.domain.accommodation.service;

import com.group6.accommodation.domain.accommodation.model.dto.AccommodationDetailResponseDto;
import com.group6.accommodation.domain.accommodation.model.dto.AccommodationResponseDto;
import com.group6.accommodation.domain.accommodation.service.command.AccommodationSearchCommand;
import com.group6.accommodation.domain.accommodation.service.specification.AccommodationSpecification;
import com.group6.accommodation.domain.room.repository.RoomRepository;
import com.group6.accommodation.global.model.dto.PagedDto;
import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.accommodation.model.enums.Area;
import com.group6.accommodation.domain.accommodation.model.enums.Category;
import com.group6.accommodation.domain.accommodation.repository.AccommodationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;
    private final RoomRepository roomRepository;

    // 숙소 전체 조회 or 테마별 조회
    public PagedDto<AccommodationResponseDto> findByCategoryOrAll(String category, int page) {
        String categoryCode = Category.getCodeByName(category);
        Page<AccommodationEntity> accommodationPage = accommodationRepository.findByCategoryWithNullCheck(categoryCode, PageRequest.of(page, 12, Sort.by(Sort.Direction.DESC, "likeCount")));

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
        AccommodationEntity accommodation = accommodationRepository.getById(id);

        return AccommodationDetailResponseDto.fromEntity(accommodation, this);
    }

    // 키워드로 숙소 조회
    public PagedDto<AccommodationResponseDto> findByKeywordPaged(String keyword, int page) {
        int customSize = 9;
        Page<AccommodationEntity> accommodationPage = accommodationRepository.findByTitleOrAddressContainingKeyword(keyword, PageRequest.of(page, customSize, Sort.by(Sort.Direction.DESC, "likeCount")));

        return getPagedDto(accommodationPage);

    }

    // 조건에 부합하는 숙소 조회
    public PagedDto<AccommodationResponseDto> findAvaliableAccommodation(AccommodationSearchCommand command) {
        Specification<AccommodationEntity> spec = AccommodationSpecification.findAvailableAccommodation(command);

        PageRequest pageRequest = PageRequest.of(command.getPage(), 9, Sort.by(Sort.Direction.DESC, "likeCount"));
        List<AccommodationEntity> allAccommodation = accommodationRepository.findAll(spec);
        Page<AccommodationEntity> accommodationPage = accommodationRepository.findAllWithCountQuery(allAccommodation, pageRequest);
        return getPagedDto(accommodationPage);
    }

    // 해당 숙소의 객실 중 가장 저렴한 객실 가격 출력
    public Integer getMinRoomPrice(Long accommodationId) {
        return roomRepository.findMinWeekDaysFeeByAccommodation_Id(accommodationId);
    }

    // Page 정보값 포함한 PagedDto로 변환.(공통 로직)
    public PagedDto getPagedDto(Page<AccommodationEntity> entity) {
        PagedDto pagedDto = new PagedDto<>(
                (int) entity.getTotalElements(),
                entity.getTotalPages(),
                entity.getSize(),
                entity.getNumber(),
                AccommodationResponseDto.fromEntites(entity.getContent(), this)
        );
        return pagedDto;
    }

    // Open api에서 불러온 데이터 저장
    @Transactional
    public void saveAccommodations(List<AccommodationEntity> accommodations) {
        // Accommodation의 Id를 모두 가져온다.
        Set<Long> accommodationIds = accommodations.stream()
                .map(AccommodationEntity::getId)
                .collect(Collectors.toSet());

        // 모든 ID를 통해 모든 Accommodation 정보를 불러온다.
        Map<Long, AccommodationEntity> existingAccommodations = accommodationRepository.findAllByIdIn(accommodationIds).stream()
                .collect(Collectors.toMap(AccommodationEntity::getId, Function.identity()));

        for (AccommodationEntity accommodation : accommodations) {
            AccommodationEntity existingAccommodation = existingAccommodations.get(accommodation.getId());
            if (existingAccommodation != null) {
                // 업데이트 로직
                existingAccommodation.update(accommodation);
            } else {
                // 새로운 데이터 추가
                accommodationRepository.save(accommodation);
            }
        }
    }
}
