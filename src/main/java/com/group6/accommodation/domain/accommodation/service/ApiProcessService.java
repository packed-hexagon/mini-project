package com.group6.accommodation.domain.accommodation.service;

import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.accommodation.repository.AccommodationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiProcessService {

    private final AccommodationApiService accommodationApiService;
    private final AccommodationService accommodationService;
    private final AccommodationRepository accommodationRepository;

    public void processAccommodations() {
        List<AccommodationEntity> accommodations = accommodationApiService.fetchAllAccommodations();
        accommodationService.saveAccommodations(accommodations);
    }

    public boolean isDatabaseEmpty() {
        return accommodationRepository.count() == 0;
    }

}