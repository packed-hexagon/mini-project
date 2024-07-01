package com.group6.accommodation.domain.accommodation.controller;

import com.group6.accommodation.domain.accommodation.model.dto.AccommodationDetailResponseDto;
import com.group6.accommodation.domain.accommodation.model.dto.AccommodationResponseDto;
import com.group6.accommodation.domain.accommodation.annotation.ValidArea;
import com.group6.accommodation.domain.accommodation.annotation.ValidCategory;
import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.global.model.dto.PagedDto;
import com.group6.accommodation.domain.accommodation.service.AccommodationService;
import com.group6.accommodation.global.util.Response;
import com.group6.accommodation.global.util.ResponseApi;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/open-api")
public class AccommodationController {

    private final AccommodationService accommodationService;

    @GetMapping("/accommodation")
    public ResponseEntity<ResponseApi<PagedDto<AccommodationResponseDto>>> readAll(
            @RequestParam(defaultValue = "0") int page,
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
            @RequestParam(defaultValue = "0") int page
    ) {

        PagedDto<AccommodationResponseDto> accommodationPage = accommodationService.findByKeywordPaged(keyword, page);
        return ResponseEntity.ok(ResponseApi.success(HttpStatus.OK, accommodationPage));
    }

    @GetMapping("/accommodation/search")
    public ResponseEntity<ResponseApi<PagedDto<AccommodationResponseDto>>> searchAccommodations(
            @RequestParam String area,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam int headcount,
            @RequestParam(defaultValue = "0") int page) {

        PagedDto<AccommodationResponseDto> accommodationPage = accommodationService.findAvaliableAccommodation(area, startDate, endDate, headcount, page);

        return ResponseEntity.ok(ResponseApi.success(HttpStatus.OK, accommodationPage));
    }
}
