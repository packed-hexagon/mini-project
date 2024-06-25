package com.group6.accommodation.domain.accommodation.controller;

import com.group6.accommodation.domain.accommodation.model.dto.AccommodationDetailResponseDto;
import com.group6.accommodation.domain.accommodation.model.dto.AccommodationResponseDto;
import com.group6.accommodation.global.model.dto.PagedDto;
import com.group6.accommodation.domain.accommodation.service.AccommodationService;
import com.group6.accommodation.global.exception.error.AccommodationErrorCode;
import com.group6.accommodation.global.exception.type.AccommodationException;
import com.group6.accommodation.global.util.ResponseApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/open-api")
public class AccommodationController {

    private final AccommodationService accommodationService;

    // 숙소 전체 조회
    @GetMapping(path = "/accommodation")
    public ResponseEntity<ResponseApi<PagedDto<AccommodationResponseDto>>> readAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String area,
            @RequestParam(required = false) String category
    ) {

        ResponseApi<PagedDto<AccommodationResponseDto>> accommodationPage;

        // 임시 기본 사이즈 설정(전체일 경우 12, 종류/지역 선택별 조회일 경우 9)
        int customSize = (area == null && category == null) ? 12 : 9;

        if (area != null && category == null) {
            accommodationPage = accommodationService.findByAreaPaged(area, page, customSize);
        } else if (category != null && area == null) {
            accommodationPage = accommodationService.findByCategoryPaged(category, page, customSize);
        } else if (area == null && category == null){
            accommodationPage = accommodationService.findAllPage(page, customSize);
        } else {
            throw new AccommodationException(
                    AccommodationErrorCode.NOT_BOTH_AREA_CATEGORY);
        }

        return ResponseEntity.status(HttpStatus.OK).body(accommodationPage);
    }

    // 숙소 단건 조회
    @GetMapping(path = "/accommodation/{id}")
    public ResponseEntity<ResponseApi<AccommodationDetailResponseDto>> read(
            @PathVariable(name = "id") Long id
    ) {
        ResponseApi<AccommodationDetailResponseDto> accommodationDetail = accommodationService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(accommodationDetail);
    }

    // 키워드로 숙소 조회
    @GetMapping(path = "/accommodation/search")
    public ResponseEntity<ResponseApi<PagedDto<AccommodationResponseDto>>> searchByKeyword(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page
    ) {
        int customSize = 9; // 기본 페이지 사이즈 설정

        ResponseApi<PagedDto<AccommodationResponseDto>> accommodationPage = accommodationService.findByKeywordPaged(keyword, page, customSize);
        return ResponseEntity.status(HttpStatus.OK).body(accommodationPage);
    }
}
