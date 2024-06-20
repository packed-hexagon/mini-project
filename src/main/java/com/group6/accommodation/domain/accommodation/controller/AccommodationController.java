package com.group6.accommodation.domain.accommodation.controller;

import com.group6.accommodation.domain.accommodation.model.dto.AccommodationDetailDto;
import com.group6.accommodation.domain.accommodation.model.dto.AccommodationDto;
import com.group6.accommodation.domain.accommodation.model.dto.PagedDto;
import com.group6.accommodation.domain.accommodation.service.AccommodationService;
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
    public ResponseEntity<ResponseApi<PagedDto<AccommodationDto>>> readAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        ResponseApi<PagedDto<AccommodationDto>> accommodationPage = accommodationService.findAllPage(page, size);

        return ResponseEntity.status(HttpStatus.OK).body(accommodationPage);
    }

    // 숙소 단건 조회
    @GetMapping(path = "/accommodation/{id}")
    public ResponseEntity<ResponseApi<AccommodationDetailDto>> read(
            @PathVariable(name = "id") Long id
    ) {
        ResponseApi<AccommodationDetailDto> accommodationDetail = accommodationService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(accommodationDetail);
    }
}
