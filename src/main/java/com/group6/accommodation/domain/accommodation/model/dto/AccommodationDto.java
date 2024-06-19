package com.group6.accommodation.domain.accommodation.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccommodationDto {

    private Long id;
    private String title;
    private String address;
    private String address2;
    private String category;
    private String image;
    private String thumbnail;
    private int likeCount;

}
