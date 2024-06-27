package com.group6.accommodation.domain.likes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group6.accommodation.domain.accommodation.model.dto.AccommodationResponseDto;
import com.group6.accommodation.domain.likes.model.dto.UserLikeResponseDto;
import com.group6.accommodation.domain.likes.service.UserLikeService;
import com.group6.accommodation.global.model.dto.PagedDto;
import com.group6.accommodation.global.security.service.CustomUserDetails;
import com.group6.accommodation.global.util.ResponseApi;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class UserLikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserLikeService userLikeService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private CustomUserDetails testUser;

    @BeforeEach
    public void setUp() {
        testUser = new CustomUserDetails(1L, "testUser", "password");
    }

    @Test
    @DisplayName("찜 하기")
    public void testAddLike() throws Exception {
        Long accommodationId = 1L;
        UserLikeResponseDto userLikeResponseDto = new UserLikeResponseDto(accommodationId, testUser.getUserId());

        Mockito.when(userLikeService.addLike(accommodationId, testUser.getUserId()))
            .thenReturn(userLikeResponseDto);

        mockMvc.perform(post("/api/user-like/{accommodationId}", accommodationId)
            .with(user(testUser))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.accommodationId", is(1)))
            .andExpect(jsonPath("$.data.userId", is(1)));
    }


    @Test
    @DisplayName("찜 삭제")
    public void testCancelLike() throws Exception {
        Long accommodationId = 1L;
        String responseMessage = "Delete Success";

        Mockito.when(userLikeService.cancelLike(accommodationId, testUser.getUserId()))
            .thenReturn(responseMessage);

        mockMvc.perform(delete("/api/user-like/{accommodationId}", accommodationId)
            .with(user(testUser))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data", is("Delete Success")));
    }

    @Test
    @DisplayName("찜 목록 조회")
    public void testGetLikedAccommodation() throws Exception {
        PagedDto<AccommodationResponseDto> pagedDto = new PagedDto<>();
        pagedDto.setTotalElements(10);
        pagedDto.setTotalPages(2);
        pagedDto.setSize(5);
        pagedDto.setCurrentPage(0);
        pagedDto.setContent(Arrays.asList(
            AccommodationResponseDto.builder().id(1L).title("Accommodation 1").build(),
            AccommodationResponseDto.builder().id(2L).title("Accommodation 2").build()
        ));

        Mockito.when(userLikeService.getLikedAccommodation(testUser.getUserId(), 0, 5))
            .thenReturn(pagedDto);

        mockMvc.perform(get("/api/user-like", 0, 5)
            .with(user(testUser))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.resultCode").value(HttpStatus.OK.value()))
            .andExpect(jsonPath("$.resultMessage").value("OK"))
            .andExpect(jsonPath("$.data.totalElements").value(10))
            .andExpect(jsonPath("$.data.totalPages").value(2))
            .andExpect(jsonPath("$.data.size").value(5))
            .andExpect(jsonPath("$.data.currentPage").value(0))
            .andExpect(jsonPath("$.data.content[0].id").value(1))
            .andExpect(jsonPath("$.data.content[0].title").value("Accommodation 1"))
            .andExpect(jsonPath("$.data.content[1].id").value(2))
            .andExpect(jsonPath("$.data.content[1].title").value("Accommodation 2"));
    }
}
