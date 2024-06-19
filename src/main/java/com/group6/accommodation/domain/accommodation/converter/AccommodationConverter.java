package com.group6.accommodation.domain.accommodation.converter;

import com.group6.accommodation.domain.accommodation.model.dto.AccommodationDto;
import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
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
                .category(accommodation.getCategory())
                .image(accommodation.getImage())
                .thumbnail(accommodation.getThumbnail())
                .likeCount(accommodation.getLikeCount()).build();
    }

    public List<AccommodationDto> toDtoList(List<AccommodationEntity> accommodationEntityList) {
        return accommodationEntityList.stream().map(this::toDto).collect(Collectors.toList());
    }
}
