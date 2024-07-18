package com.group6.accommodation.global.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagedDto<T> {
    // 총 데이터 수
    private int totalElements;

    // 총 페이지 수
    private int totalPages;

    // 한 페이지 당 데이터 수
    private int size;

    // 현재 페이지
    private int currentPage;

    // 숙소 정보 List
    private List<T> content;


    public static <T> PagedDto<T> from(Page<T> page) {
        return new PagedDto<>(
            (int) page.getTotalElements(),
            page.getTotalPages(),
            page.getSize(),
            page.getNumber(),
            page.getContent()
        );
    }
}
