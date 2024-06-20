package com.group6.accommodation.domain.accommodation.service;

import com.group6.accommodation.domain.accommodation.converter.AccommodationConverter;
import com.group6.accommodation.domain.accommodation.model.dto.AccommodationDetailDto;
import com.group6.accommodation.domain.accommodation.model.dto.AccommodationDto;
import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.accommodation.repository.AccommodationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;
    private final AccommodationConverter accommodationConverter;

    public List<AccommodationDto> findAll() {
        List<AccommodationEntity> accommodationEntityList = accommodationRepository.findAll();

        if(accommodationEntityList.isEmpty()) {
            throw new NoSuchElementException("No Accommodation");
        }
        return accommodationConverter.toDtoList(accommodationEntityList);
    }

    public AccommodationDetailDto findById(Long id) {
        AccommodationEntity accommodationEntity = accommodationRepository.findById(id).get();

        if(accommodationEntity == null) {
            throw new NoSuchElementException("No ID");
        }
        return accommodationConverter.toDetailDto(accommodationEntity);
    }


}
