package com.group6.accommodation.domain.reservation.controller;

import com.group6.accommodation.domain.reservation.model.dto.PostReserveRequestDto;
import com.group6.accommodation.domain.reservation.model.dto.PostReserveResponseDto;
import com.group6.accommodation.domain.reservation.service.ReserveService;
import com.group6.accommodation.global.util.ResponseApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class ReserveController {

    private final ReserveService reserveService;

    @PostMapping("/{accommodationId}/room/{roomId}/reserve")
    public ResponseEntity<ResponseApi<PostReserveResponseDto>> postReserve(
        @PathVariable Long accommodationId,
        @PathVariable Long roomId,
        @RequestBody PostReserveRequestDto requestDto
    ) {
        ResponseApi<PostReserveResponseDto> responseData = reserveService.postReserve(accommodationId, roomId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseData);
    }
}
