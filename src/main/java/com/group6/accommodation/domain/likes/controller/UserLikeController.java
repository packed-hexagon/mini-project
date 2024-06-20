package com.group6.accommodation.domain.likes.controller;

import com.group6.accommodation.domain.likes.service.UserLikeService;
import com.group6.accommodation.global.util.Response;
import lombok.RequiredArgsConstructor;
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
    public Response<?> addLikes (
        @PathVariable("accommodationId") Long accommodationID
//        @AuthenticationPrincipal User user
    ) {
//        var loginUserId = Long.parseLong(user.getUserId());
        var addLike = userLikeService.addLikes(accommodationID, 1L);
        return Response.builder()
            .resultCode("201")
            .resultMessage("Created")
            .data(addLike)
            .build()
            ;
    }

    @DeleteMapping("/{accommodationId}")
    public Response<?> removeLikes(
        @PathVariable Long accommodationID
//        @AuthenticationPrincipal User user
    ) {
//        var loginUserId = Long.parseLong(user.getUserId());
        var cancelLike = userLikeService.cancelLikes(accommodationID, 1L);
        return Response.builder()
            .resultCode("204")
            .resultMessage("No Content")
            .data(cancelLike)
            .build()
            ;
    }
}
