package com.group6.accommodation.domain.accommodation.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Profile("openapi")
public class MainService {

    private final AccommodationApiService accommodationApiService;
    private final AccommodationService accommodationService;

    public void processAccommodations() throws URISyntaxException, JsonProcessingException {
        List<AccommodationEntity> accommodations = accommodationApiService.fetchAllAccommodations();
        accommodationService.saveAccommodations(accommodations);
    }
}