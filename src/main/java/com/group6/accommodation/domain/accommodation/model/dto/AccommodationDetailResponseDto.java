package com.group6.accommodation.domain.accommodation.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationDetailResponseDto {

    private Long id;
    private String title;
    private String address;
    private String areacode;
    private String category;
    private String image;
    private String thumbnail;
    private int price;
    private double latitude;
    private double longitude;
    private String tel;
    private int likeCount;
    private int reviewCount;
    private double totalRating;
}
