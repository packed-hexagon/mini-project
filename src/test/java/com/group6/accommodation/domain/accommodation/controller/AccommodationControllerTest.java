package com.group6.accommodation.domain.accommodation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group6.accommodation.AccommodationApplication;
import com.group6.accommodation.domain.accommodation.model.dto.AccommodationDetailResponseDto;
import com.group6.accommodation.domain.accommodation.model.dto.AccommodationResponseDto;
import com.group6.accommodation.domain.accommodation.service.AccommodationService;
import com.group6.accommodation.domain.accommodation.service.DatabaseInitializationService;
import com.group6.accommodation.global.exception.error.AccommodationErrorCode;
import com.group6.accommodation.global.exception.type.AccommodationException;
import com.group6.accommodation.global.model.dto.PagedDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(value = AccommodationController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@Import(AccommodationApplication.class)
class AccommodationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccommodationService accommodationService;

    @MockBean
    private DatabaseInitializationService databaseInitializationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("숙소 리스트 조회 테스트")
    void readAll() throws Exception {
        PagedDto<AccommodationResponseDto> pagedDto = new PagedDto<>();
        when(accommodationService.findByParameter(anyString(), anyString(), anyInt())).thenReturn(pagedDto);

        mockMvc.perform(get("/open-api/accommodation")
                .param("page", "0")
                .param("area", "서울")
                .param("category", "호텔"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultMessage").value("OK"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @DisplayName("유효하지 않은 지역명으로 숙소 리스트 조회 시 예외 발생 테스트")
    void readWithInvalidArea() throws Exception {
        mockMvc.perform(get("/open-api/accommodation")
                .param("area", "잘못된지역"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("유효하지 않은 테마명으로 숙소 리스트 조회 시 예외 발생 테스트")
    void readWithInvalidCategory() throws Exception {
        mockMvc.perform(get("/open-api/accommodation")
                .param("category", "잘못된테마"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("숙소 단건 조회 테스트")
    void read() throws Exception {
        AccommodationDetailResponseDto detailDto = new AccommodationDetailResponseDto();
        when(accommodationService.findById(any())).thenReturn(detailDto);

        mockMvc.perform(get("/open-api/accommodation/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultMessage").value("OK"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @DisplayName("존재하지 않은 숙소 ID로 조회 시 예외 발생 테스트")
    void readWithInvalidId() throws Exception {
        when(accommodationService.findById(any())).thenThrow(new AccommodationException(AccommodationErrorCode.NOT_FOUND_ACCOMMODATION));

        mockMvc.perform(get("/open-api/accommodation/999")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("키워드로 숙소 조회 테스트")
    void searchByKeyword() throws Exception {
        PagedDto<AccommodationResponseDto> pagedDto = new PagedDto<>();
        when(accommodationService.findByKeywordPaged(any(), anyInt())).thenReturn(pagedDto);

        mockMvc.perform(get("/open-api/accommodation/search")
                        .param("keyword", "호텔")
                        .param("page", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultMessage").value("OK"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @DisplayName("여러 조건들로 숙소 조회 테스트")
    void searchByCondition() throws Exception {
        PagedDto<AccommodationResponseDto> pagedDto = new PagedDto<>();
        when(accommodationService.findAvaliableAccommodation(any(), any(), any(), anyInt(), anyInt())).thenReturn(pagedDto);

        mockMvc.perform(get("/open-api/accommodation/condition")
                    .param("area", "경남")
                    .param("startDate", "2024-07-08")
                    .param("endDate", "2024-07-10")
                    .param("headcount", "2")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultMessage").value("OK"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @DisplayName("조건부 숙소 조회 시 날짜 유효성 검사 실패 테스트")
    void searchByConditionWithInvalidDate() throws Exception {
        mockMvc.perform(get("/open-api/accommodation/condition")
                    .param("area", "서울")
                    .param("startDate", LocalDate.now().minusDays(1).toString())
                    .param("endDate", LocalDate.now().plusDays(3).toString())
                    .param("headcount", "2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("페이지가 음수일 경우 예외 처리 테스트")
    void readWithNegativePage() throws Exception {
        mockMvc.perform(get("/open-api/accommodation")
                    .param("page", "-1"))
                .andExpect(status().isBadRequest());
    }
}