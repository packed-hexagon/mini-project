package com.group6.accommodation.domain.accommodation.model.dto;

import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.accommodation.model.enums.Area;
import com.group6.accommodation.domain.accommodation.model.enums.Category;
import com.group6.accommodation.domain.accommodation.service.AccommodationService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationResponseDto {

    private Long id;
    private String title;
    private String address;
    private String area;
    private String category;
    private String image;
    private String thumbnail;
    private Integer price;
    private double latitude;
    private double longitude;
    private int likeCount;
    private int reviewCount;
    private double totalRating;

    public static AccommodationResponseDto fromEntity(AccommodationEntity entity, Integer minRommPrice) {
        return new AccommodationResponseDto(
                entity.getId(),
                entity.getTitle(),
                entity.getAddress(),
                Area.getNameByCode(entity.getAreacode()),
                Category.getNameByCode(entity.getCategory()),
                entity.getImage(),
                entity.getThumbnail(),
                minRommPrice,
                entity.getLatitude(),
                entity.getLongitude(),
                entity.getLikeCount(),
                entity.getReviewCount(),
                entity.getTotalRating()
        );
    }

    public static List<AccommodationResponseDto> fromEntites(List<AccommodationEntity> entities, AccommodationService accommodationService) {
        return entities.stream()
                .map(entity -> fromEntity(entity, accommodationService.getMinRoomPrice(entity.getId())))
                .collect(Collectors.toList());
    }

}
