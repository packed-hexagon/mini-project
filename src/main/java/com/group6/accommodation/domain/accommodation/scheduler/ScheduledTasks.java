package com.group6.accommodation.domain.accommodation.scheduler;

import com.group6.accommodation.domain.accommodation.service.ApiProcessService;
import com.group6.accommodation.domain.room.service.ApiProcessRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final ApiProcessService apiProcessService;
    private final ApiProcessRoomService apiProcessRoomService;

    // 매일 오전 6시 실행
    @Scheduled(cron = "0 0 6 * * ?")
    public void updateAccommodations() {
        try {
            apiProcessService.processAccommodations();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 0 6 * * ?")
    public void updateRooms() {
        try {
            apiProcessRoomService.processRooms();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
