package com.group6.accommodation.domain.accommodation.model.dto;

import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.accommodation.model.enums.Area;
import com.group6.accommodation.domain.accommodation.model.enums.Category;
import com.group6.accommodation.domain.accommodation.service.AccommodationService;
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
    private Integer price;
    private double latitude;
    private double longitude;
    private String tel;
    private int likeCount;
    private int reviewCount;
    private double totalRating;

    public static AccommodationDetailResponseDto fromEntity(AccommodationEntity entity, AccommodationService accommodationService) {
        return new AccommodationDetailResponseDto(
                entity.getId(),
                entity.getTitle(),
                entity.getAddress(),
                Area.getNameByCode(entity.getAreacode()),
                Category.getNameByCode(entity.getCategory()),
                entity.getImage(),
                entity.getThumbnail(),
                accommodationService.getMinRoomPrice(entity.getId()),
                entity.getLatitude(),
                entity.getLongitude(),
                entity.getTel(),
                entity.getLikeCount(),
                entity.getReviewCount(),
                entity.getTotalRating()
        );
    }
}
