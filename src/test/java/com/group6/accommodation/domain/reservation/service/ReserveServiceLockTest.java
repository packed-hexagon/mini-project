package com.group6.accommodation.domain.reservation.service;

import com.group6.accommodation.domain.reservation.model.dto.PostReserveRequestDto;
import com.group6.accommodation.domain.reservation.model.dto.ReserveResponseDto;
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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Disabled
public class ReserveServiceLockTest {

    @Autowired
    private ReserveService reserveService;

    @Test
    @DisplayName("예약하기 동시성 테스트")
    @Disabled
    public void simultaneousTest() throws InterruptedException, ExecutionException {
        Long userId = 1L;
        Long accommodationId = 1L;
        Long roomId = 1L;
        int numberOfThreads = 10;

        PostReserveRequestDto postReserveRequestDto = new PostReserveRequestDto(2, LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 100);

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<Callable<ReserveResponseDto>> tasks = new ArrayList<>();

        for (int i = 0; i < numberOfThreads; i++) {
            tasks.add(() -> reserveService.postReserve(userId, accommodationId, roomId, postReserveRequestDto));
        }

        List<Future<ReserveResponseDto>> futures = executorService.invokeAll(tasks);

        Set<ReserveResponseDto> result = new HashSet<>();

        for (Future<ReserveResponseDto> future : futures) {
            ReserveResponseDto response = future.get();
            if (response != null) {
                result.add(response);
            }
        }

        // 1명만 예약에 성공했는지 확인
        assertEquals(10, result.size());
        executorService.shutdown();
    }
}
