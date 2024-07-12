package com.group6.accommodation.domain.accommodation.converter;

import com.group6.accommodation.domain.accommodation.model.dto.AccommodationDetailResponseDto;
import com.group6.accommodation.domain.accommodation.model.dto.AccommodationResponseDto;
import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.accommodation.model.enums.Area;
import com.group6.accommodation.domain.accommodation.model.enums.Category;
import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import com.group6.accommodation.domain.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccommodationConverter {

    private final RoomRepository roomRepository;

    public AccommodationResponseDto toDto(AccommodationEntity accommodation) {
        int minPrice = getMinRoomPrice(accommodation.getId());

        return AccommodationResponseDto.builder()
                .id(accommodation.getId())
                .title(accommodation.getTitle())
                .address(accommodation.getAddress())
                .area(Area.getNameByCode(accommodation.getAreacode()))
                .category(Category.getNameByCode(accommodation.getCategory()))
                .image(accommodation.getImage())
                .thumbnail(accommodation.getThumbnail())
                .price(minPrice)
                .latitude(accommodation.getLatitude())
                .longitude(accommodation.getLongitude())
                .likeCount(accommodation.getLikeCount())
                .reviewCount(accommodation.getReviewCount())
                .totalRating(accommodation.getTotalRating())
                .build();
    }

    public List<AccommodationResponseDto> toDtoList(List<AccommodationEntity> accommodationEntityList) {
        return accommodationEntityList.stream().map(this::toDto).collect(Collectors.toList());
    }

    public AccommodationDetailResponseDto toDetailDto(AccommodationEntity accommodation) {
        int minPrice = getMinRoomPrice(accommodation.getId());

        return AccommodationDetailResponseDto.builder()
                .id(accommodation.getId())
                .title(accommodation.getTitle())
                .address(accommodation.getAddress())
                .areacode(Area.getNameByCode(accommodation.getAreacode()))
                .category(Category.getNameByCode(accommodation.getCategory()))
                .image(accommodation.getImage())
                .thumbnail(accommodation.getThumbnail())
                .price(minPrice)
                .latitude(accommodation.getLatitude())
                .longitude(accommodation.getLongitude())
                .tel(accommodation.getTel())
                .likeCount(accommodation.getLikeCount())
                .reviewCount(accommodation.getReviewCount())
                .totalRating(accommodation.getTotalRating())
                .build();
    }

    private int getMinRoomPrice(Long accommodationId) {
        List<RoomEntity> byAccommodationId = roomRepository.findByAccommodation_Id(accommodationId);
        int minPrice = Integer.MAX_VALUE;

        for(RoomEntity room : byAccommodationId) {
            if(minPrice > room.getRoomOffseasonMinfee1()) {
                minPrice = room.getRoomOffseasonMinfee1();
            }
        }
        return minPrice;
    }
}
