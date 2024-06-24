package com.group6.accommodation.domain.reservation.controller;

import com.group6.accommodation.domain.reservation.model.dto.ReserveListItemDto;
import com.group6.accommodation.global.model.dto.PagedDto;
import com.group6.accommodation.domain.reservation.model.dto.PostReserveRequestDto;
import com.group6.accommodation.domain.reservation.model.dto.ReserveResponseDto;
import com.group6.accommodation.domain.reservation.service.ReserveService;
import com.group6.accommodation.global.util.ResponseApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class ReserveController {

    private final ReserveService reserveService;

    @PostMapping("/{accommodationId}/room/{roomId}/reserve")
    public ResponseEntity<ResponseApi<ReserveResponseDto>> postReserve(
        @PathVariable Long accommodationId,
        @PathVariable Long roomId,
        @RequestBody PostReserveRequestDto requestDto
    ) {
        ResponseApi<ReserveResponseDto> responseData = reserveService.postReserve(accommodationId, roomId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseData);
    }


    @PutMapping("/{reservationId}")
    public ResponseEntity<ResponseApi<ReserveResponseDto>> cancelReserve(@PathVariable Long reservationId) {
        ResponseApi<ReserveResponseDto> responseData = reserveService.cancelReserve(reservationId);
        return ResponseEntity.ok(responseData);
    }


    @GetMapping
    public ResponseEntity<ResponseApi<PagedDto<ReserveListItemDto>>> getList(
        @RequestParam(name = "page") int page,
        @RequestParam(name = "size") int size,
        @RequestParam(name = "direction") String direction

    ) {
        ResponseApi<PagedDto<ReserveListItemDto>> responseData = reserveService.getList(page, size, direction);
        return ResponseEntity.ok(responseData);
    }
}
