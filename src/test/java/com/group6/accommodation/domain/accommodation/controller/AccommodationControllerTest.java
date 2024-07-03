package com.group6.accommodation.domain.accommodation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group6.accommodation.AccommodationApplication;
import com.group6.accommodation.domain.accommodation.model.dto.AccommodationDetailResponseDto;
import com.group6.accommodation.domain.accommodation.model.dto.AccommodationResponseDto;
import com.group6.accommodation.domain.accommodation.service.AccommodationService;
import com.group6.accommodation.domain.accommodation.service.DatabaseInitializationService;
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
    @DisplayName("숙소 리스트 조회")
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
}