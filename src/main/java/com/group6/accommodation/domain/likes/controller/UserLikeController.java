package com.group6.accommodation.domain.likes.controller;

import com.group6.accommodation.domain.accommodation.model.dto.AccommodationResponseDto;
import com.group6.accommodation.domain.likes.model.dto.UserLikeResponseDto;
import com.group6.accommodation.domain.likes.service.UserLikeService;
import com.group6.accommodation.global.model.dto.PagedDto;
import com.group6.accommodation.global.security.service.CustomUserDetails;
import com.group6.accommodation.global.util.ResponseApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/user-like")
@RequiredArgsConstructor
@RestController
public class UserLikeController {

    private final UserLikeService userLikeService;

    // 숙박시설 찜하기
    @PostMapping("/{accommodationId}")
    public ResponseEntity<ResponseApi<UserLikeResponseDto>> addLike (
        @PathVariable("accommodationId") Long accommodationID,
        @AuthenticationPrincipal CustomUserDetails user
    ) {
        var loginUserId = user.getUserId();
        ResponseApi<UserLikeResponseDto> response = userLikeService.addLike(accommodationID, loginUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 숙박시설 찜 삭제하기
    @DeleteMapping("/{accommodationId}")
    public ResponseEntity<ResponseApi<String>> cancelLike(
        @PathVariable("accommodationId") Long accommodationID,
        @AuthenticationPrincipal CustomUserDetails user
    ) {
        var loginUserId = user.getUserId();
        ResponseApi<String> response = userLikeService.cancelLike(accommodationID, loginUserId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 찜한 숙박 목록
    @GetMapping
    public ResponseEntity<ResponseApi<PagedDto<AccommodationResponseDto>>> getLikedAccommodation(
        @AuthenticationPrincipal CustomUserDetails user,
        @RequestParam(name ="page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "5") int size
    ) {
        var loginUserId = user.getUserId();
        ResponseApi<PagedDto<AccommodationResponseDto>> response = userLikeService.getLikedAccommodation(loginUserId, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
