package com.group6.accommodation.domain.accommodation.controller;

import com.group6.accommodation.domain.accommodation.model.dto.AccommodationDto;
import com.group6.accommodation.domain.accommodation.service.AccommodationService;
import com.group6.accommodation.global.util.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/open-api")
public class AccommodationController {

    private final AccommodationService accommodationService;

    @GetMapping(path = "/accommodation")
    public Response<List<AccommodationDto>> readAll() {
        List<AccommodationDto> accommodationList = accommodationService.findAll();

        return Response.<List<AccommodationDto>>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(accommodationList)
                .build();
    }


}
