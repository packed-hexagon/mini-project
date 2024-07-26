package com.group6.accommodation.domain.accommodation.controller;

import com.group6.accommodation.domain.accommodation.model.dto.AccommodationConditionRequestDto;
import com.group6.accommodation.domain.accommodation.model.dto.AccommodationDetailResponseDto;
import com.group6.accommodation.domain.accommodation.model.dto.AccommodationResponseDto;
import com.group6.accommodation.global.model.dto.PagedDto;
import com.group6.accommodation.domain.accommodation.service.AccommodationService;
import com.group6.accommodation.global.util.ResponseApi;
import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "Accommodation", description = "숙박 관련 API")
public class AccommodationController {

    private final AccommodationService accommodationService;

    // 숙소 전체 조회 or 테마별 조회
    @GetMapping("/accommodation")
    @Operation(summary = "숙박시설 전체 or 테마별 조회")
    public ResponseEntity<ResponseApi<PagedDto<AccommodationResponseDto>>> readAll(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.") int page
    ) {

        PagedDto<AccommodationResponseDto> response = accommodationService.findByCategoryOrAll(category, page);
        return ResponseEntity.ok(ResponseApi.success(HttpStatus.OK, response));
    }

    // 숙소 지역별 조회
    @GetMapping("/accommodation/region")
    @Operation(summary = "숙박시설 지역별 조회")
    public ResponseEntity<ResponseApi<PagedDto<AccommodationResponseDto>>> readByArea(
            @RequestParam(required = false) String area,
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.") int page
    ) {

        PagedDto<AccommodationResponseDto> response = accommodationService.findByAreaPaged(area, page, 9);
        return ResponseEntity.ok(ResponseApi.success(HttpStatus.OK, response));
    }

    // 숙소 단건 조회
    @GetMapping("/accommodation/{id}")
    @Operation(summary = "숙박시설 단일 조회")
    public ResponseEntity<ResponseApi<AccommodationDetailResponseDto>> read(
            @PathVariable Long id
    ) {

        AccommodationDetailResponseDto accommodationDetail = accommodationService.findById(id);
        return ResponseEntity.ok(ResponseApi.success(HttpStatus.OK, accommodationDetail));
    }

    // 키워드로 숙소 조회
    @GetMapping("/accommodation/search")
    @Operation(summary = "숙소시설 검색", description = "숙박시설 이름이나 주소로 숙박시설을 검색합니다.")
    public ResponseEntity<ResponseApi<PagedDto<AccommodationResponseDto>>> searchByKeyword(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.") int page
    ) {

        PagedDto<AccommodationResponseDto> accommodationPage = accommodationService.findByKeywordPaged(keyword, page);
        return ResponseEntity.ok(ResponseApi.success(HttpStatus.OK, accommodationPage));
    }

    // 위치, 날짜 범위, 인원 수 조건을 유동적으로 받아와 숙소 조회
    @GetMapping("/accommodation/condition")
    @Operation(summary = "숙박시설 조건부 검색", description = "위치, 날짜 범위, 인원 수를 조건으로 숙박시설을 검색합니다.")
    public ResponseEntity<ResponseApi<PagedDto<AccommodationResponseDto>>> searchAccommodations(
            @Valid AccommodationConditionRequestDto request
    ) {
        PagedDto<AccommodationResponseDto> accommodationPage = accommodationService.findAvaliableAccommodation(request.toCommand());
        return ResponseEntity.ok(ResponseApi.success(HttpStatus.OK, accommodationPage));
    }
}
