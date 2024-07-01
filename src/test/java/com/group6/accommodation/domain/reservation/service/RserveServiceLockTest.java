package com.group6.accommodation.domain.reservation.service;

import com.group6.accommodation.domain.reservation.model.dto.PostReserveRequestDto;
import com.group6.accommodation.domain.reservation.model.dto.ReserveResponseDto;
import com.group6.accommodation.domain.reservation.repository.ReservationRepository;
import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import com.group6.accommodation.domain.room.repository.RoomRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RserveServiceLockTest {
    @Autowired
    private ReserveService reserveService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Test
    @Transactional
    @DisplayName("예약하기 동시성 테스트")
    public void simultaneousTest() throws InterruptedException, ExecutionException {
        
        // TODO: RollBack이 필요함
        
        Long userId = 1L;
        Long accommodationId = 136213L;
        Long roomId = 331L;
        int numberOfThreads = 10;

        PostReserveRequestDto postReserveRequestDto = new PostReserveRequestDto(10, LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 800000);


        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<Callable<ReserveResponseDto>> tasks = new ArrayList<>();

        for (int i = 0; i < numberOfThreads; i++) {
            tasks.add(() -> {
                ReserveResponseDto response = reserveService.postReserve(userId,
                    accommodationId, roomId, postReserveRequestDto);
                return response;
            });
        }


        List<Future<ReserveResponseDto>> futures = executorService.invokeAll(tasks);

        Set<ReserveResponseDto> result = new HashSet<>();

        for (Future<ReserveResponseDto> future : futures) {
            ReserveResponseDto response = future.get();
            result.add(response);
        }
        
        // 개수 확인
        assertEquals(numberOfThreads, result.size());
        executorService.shutdown();

    }

}
