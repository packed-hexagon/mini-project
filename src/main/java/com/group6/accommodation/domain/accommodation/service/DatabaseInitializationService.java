package com.group6.accommodation.domain.accommodation.service;

import com.group6.accommodation.domain.room.service.ApiProcessRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseInitializationService {

    private final ApiProcessService apiProcessService;
    private final ApiProcessRoomService apiProcessRoomService;

    public void initializeDatabase() {

        if (apiProcessRoomService.isDatabaseEmpty()) {
            try {
                apiProcessRoomService.processRooms();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            apiProcessService.processAccommodations();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
