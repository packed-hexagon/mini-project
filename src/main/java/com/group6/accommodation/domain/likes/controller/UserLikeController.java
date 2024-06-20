package com.group6.accommodation.domain.likes.controller;

import com.group6.accommodation.domain.likes.model.dto.UserLikeResponseDto;
import com.group6.accommodation.domain.likes.service.UserLikeService;
import com.group6.accommodation.global.util.Response;
import com.group6.accommodation.global.util.ResponseApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/user-like")
@RequiredArgsConstructor
@RestController
public class UserLikeController {

    private final UserLikeService userLikeService;

    // 숙박시설 찜하기
    @PostMapping("/{accommodationId}")
    public ResponseEntity<ResponseApi<UserLikeResponseDto>> addLike (
        @PathVariable("accommodationId") Long accommodationID
//        @AuthenticationPrincipal User user
    ) {
//        var loginUserId = Long.parseLong(user.getUserId());
        var addLike = userLikeService.addLike(accommodationID, 1L);
        ResponseApi<UserLikeResponseDto> response = ResponseApi.success(HttpStatus.CREATED, addLike);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 숙박시설 찜 삭제하기
    @DeleteMapping("/{accommodationId}")
    public ResponseEntity<ResponseApi<String>> cancelLike(
        @PathVariable("accommodationId") Long accommodationID
//        @AuthenticationPrincipal User user
    ) {
//        var loginUserId = Long.parseLong(user.getUserId());
        userLikeService.cancelLike(accommodationID, 1L);
        ResponseApi<String> response = ResponseApi.success(HttpStatus.NO_CONTENT, "Delete Success");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
