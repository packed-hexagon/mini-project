package com.group6.accommodation.domain.accommodation.converter;

import com.group6.accommodation.domain.accommodation.model.dto.AccommodationDetailDto;
import com.group6.accommodation.domain.accommodation.model.dto.AccommodationDto;
import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.accommodation.model.enums.Area;
import com.group6.accommodation.domain.accommodation.model.enums.Category;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccommodationConverter {

    public AccommodationDto toDto(AccommodationEntity accommodation) {
        return AccommodationDto.builder()
                .id(accommodation.getId())
                .title(accommodation.getTitle())
                .address(accommodation.getAddress())
                .address2(accommodation.getAddress2())
                .category(Category.getNameByCode(accommodation.getCategory()))
                .image(accommodation.getImage())
                .thumbnail(accommodation.getThumbnail())
                .likeCount(accommodation.getLikeCount()).build();
    }

    public List<AccommodationDto> toDtoList(List<AccommodationEntity> accommodationEntityList) {
        return accommodationEntityList.stream().map(this::toDto).collect(Collectors.toList());
    }

    public AccommodationDetailDto toDetailDto(AccommodationEntity accommodation) {
        return AccommodationDetailDto.builder()
                .id(accommodation.getId())
                .title(accommodation.getTitle())
                .address(accommodation.getAddress())
                .address2(accommodation.getAddress2())
                .areacode(Area.getNameByCode(accommodation.getAreacode()))
                .sigungucode(accommodation.getSigungucode())
                .category(Category.getNameByCode(accommodation.getCategory()))
                .image(accommodation.getImage())
                .thumbnail(accommodation.getThumbnail())
                .latitude(accommodation.getLatitude())
                .longitude(accommodation.getLongitude())
                .mlevel(accommodation.getMlevel())
                .tel(accommodation.getTel())
                .likeCount(accommodation.getLikeCount()).build();
    }
}
