package com.group6.accommodation.domain.accommodation.service;

import com.group6.accommodation.domain.room.service.ApiProcessRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DatabaseInitializationService {

    private final ApiProcessService apiProcessService;
    private final ApiProcessRoomService apiProcessRoomService;

    boolean accommodationProcessed = false;

    public void initializeDatabase() {

        // 숙소 데이터베이스가 비어있다면 실행
        if(apiProcessService.isDatabaseEmpty()) {
            try {
                apiProcessService.processAccommodations();
                accommodationProcessed = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 숙소 데이터 저장 기능이 실행했거나 숙소 데이터베이스가 비어있지 않다면 실행
        if (accommodationProcessed || !apiProcessService.isDatabaseEmpty()) {
            // 객실 데이터베이스가 비어있다면 실행
            if(apiProcessRoomService.isDatabaseEmpty()) {
                try {
                    apiProcessRoomService.processRooms();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
