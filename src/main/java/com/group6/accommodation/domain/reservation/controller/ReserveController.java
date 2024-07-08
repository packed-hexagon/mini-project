package com.group6.accommodation.domain.reservation.controller;

import com.group6.accommodation.domain.reservation.model.dto.ReserveListItemDto;
import com.group6.accommodation.global.model.dto.PagedDto;
import com.group6.accommodation.domain.reservation.model.dto.PostReserveRequestDto;
import com.group6.accommodation.domain.reservation.model.dto.ReserveResponseDto;
import com.group6.accommodation.domain.reservation.service.ReserveService;
import com.group6.accommodation.global.security.service.CustomUserDetails;
import com.group6.accommodation.global.util.ResponseApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@Tag(name = "Reservation", description = "예약 관련 API")
public class ReserveController {

    private final ReserveService reserveService;

    @PostMapping("/{accommodationId}/room/{roomId}/reserve")
    @Operation(summary = "예약하기")
    public ResponseEntity<ResponseApi<ReserveResponseDto>> postReserve(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long accommodationId,
            @PathVariable Long roomId,
            @Valid
            @RequestBody PostReserveRequestDto requestDto
    ) {
        ReserveResponseDto responseData = reserveService.postReserve(user.getUserId(), accommodationId, roomId,
                requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseApi.success(HttpStatus.CREATED, responseData));
    }


    @PutMapping("/{reservationId}")
    @Operation(summary = "예약 취소하기")
    public ResponseEntity<ResponseApi<ReserveResponseDto>> cancelReserve(
            @PathVariable Long reservationId
    ) {
        ReserveResponseDto responseData = reserveService.cancelReserve(reservationId);
        return ResponseEntity.ok(ResponseApi.success(HttpStatus.OK, responseData));
    }


    @GetMapping
    @Operation(summary = "예약 리스트 조회", description = "사용자가 예약한 숙소시설의 예약들을 조회합니다.")
    public ResponseEntity<ResponseApi<PagedDto<ReserveListItemDto>>> getList(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size,
            @RequestParam(name = "direction") String direction

    ) {
        PagedDto<ReserveListItemDto> responseData = reserveService.getList(user.getUserId(), page, size, direction);
        return ResponseEntity.ok(ResponseApi.success(HttpStatus.OK, responseData));
    }
}
