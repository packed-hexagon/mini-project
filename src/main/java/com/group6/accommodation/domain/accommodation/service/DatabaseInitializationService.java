package com.group6.accommodation.domain.accommodation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseInitializationService {

    private final ApiProcessService apiProcessService;

    public void initializeDatabase() {
        if (apiProcessService.isDatabaseEmpty()) {
            try {
                apiProcessService.processAccommodations();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
