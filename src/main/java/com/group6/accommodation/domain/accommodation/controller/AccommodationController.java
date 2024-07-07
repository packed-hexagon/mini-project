package com.group6.accommodation.domain.accommodation.controller;

import com.group6.accommodation.domain.accommodation.model.dto.AccommodationConditionRequestDto;
import com.group6.accommodation.domain.accommodation.model.dto.AccommodationDetailResponseDto;
import com.group6.accommodation.domain.accommodation.model.dto.AccommodationResponseDto;
import com.group6.accommodation.domain.accommodation.annotation.ValidArea;
import com.group6.accommodation.domain.accommodation.annotation.ValidCategory;
import com.group6.accommodation.global.model.dto.PagedDto;
import com.group6.accommodation.domain.accommodation.service.AccommodationService;
import com.group6.accommodation.global.util.ResponseApi;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/open-api")
@Tag(name = "accommodation", description = "숙박")
public class AccommodationController {

    private final AccommodationService accommodationService;

    @GetMapping("/accommodation")
    public ResponseEntity<ResponseApi<PagedDto<AccommodationResponseDto>>> readAll(
            @RequestParam(defaultValue = "0") @Min (value = 0, message = "페이지 번호는 0 이상이어야 합니다.") int page,
            @RequestParam(required = false) @ValidArea String area,
            @RequestParam(required = false) @ValidCategory String category
    ) {

        PagedDto<AccommodationResponseDto> response = accommodationService.findByParameter(area, category, page);
        return ResponseEntity.ok(ResponseApi.success(HttpStatus.OK, response));
    }

    // 숙소 단건 조회
    @GetMapping("/accommodation/{id}")
    public ResponseEntity<ResponseApi<AccommodationDetailResponseDto>> read(
            @PathVariable(name = "id") Long id
    ) {

        AccommodationDetailResponseDto accommodationDetail = accommodationService.findById(id);
        return ResponseEntity.ok(ResponseApi.success(HttpStatus.OK, accommodationDetail));
    }

    // 키워드로 숙소 조회
    @GetMapping( "/accommodation/search")
    public ResponseEntity<ResponseApi<PagedDto<AccommodationResponseDto>>> searchByKeyword(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.") int page
    ) {

        PagedDto<AccommodationResponseDto> accommodationPage = accommodationService.findByKeywordPaged(keyword, page);
        return ResponseEntity.ok(ResponseApi.success(HttpStatus.OK, accommodationPage));
    }

    // 위치, 날짜 범위, 인원 수 조건을 유동적으로 받아와 숙소 조회
    @GetMapping("/accommodation/condition")
    public ResponseEntity<ResponseApi<PagedDto<AccommodationResponseDto>>> searchAccommodations(
            @Valid AccommodationConditionRequestDto request
    ) {

        PagedDto<AccommodationResponseDto> accommodationPage = accommodationService.findAvaliableAccommodation(
                request.getArea(), request.getStartDate(), request.getEndDate(), request.getHeadcount(), request.getPage());
        return ResponseEntity.ok(ResponseApi.success(HttpStatus.OK, accommodationPage));
    }
}
