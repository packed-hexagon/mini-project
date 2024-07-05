package com.group6.accommodation.domain.reservation.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.group6.accommodation.domain.reservation.model.dto.PostReserveRequestDto;
import com.group6.accommodation.domain.reservation.model.dto.ReserveListItemDto;
import com.group6.accommodation.domain.reservation.model.dto.ReserveResponseDto;
import com.group6.accommodation.domain.reservation.service.ReserveService;
import com.group6.accommodation.global.model.dto.PagedDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group6.accommodation.global.security.service.CustomUserDetails;
import com.group6.accommodation.global.security.token.model.dto.LoginTokenResponseDto;
import com.group6.accommodation.global.security.token.provider.TokenProvider;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(controllers = ReserveController.class)
class ReserveControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReserveService reserveService;

    @MockBean
    private TokenProvider tokenProvider;

    private CustomUserDetails user;
    private String token;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

        // 사용자 설정
        user = new CustomUserDetails(1L, "testUser", "1234");
        Authentication auth = new UsernamePasswordAuthenticationToken(user, "1234",
            user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        // 토큰 설정
        LoginTokenResponseDto mockTokenResponse = LoginTokenResponseDto.builder()
            .accessToken("mockAccessToken")
            .build();
        when(tokenProvider.createToken(auth)).thenReturn(mockTokenResponse);

        token = tokenProvider.createToken(auth).getAccessToken();

        when(tokenProvider.getAuthentication(token)).thenReturn(auth);
    }


    @Test
    @DisplayName("예약 리스트 조회")
    public void getReservations() throws Exception {
        List<ReserveListItemDto> reservations = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            ReserveListItemDto item = ReserveListItemDto.builder()
                .id((long) i)
                .price(10000 * i)
                .accommodationTitle("테스트 숙소")
                .roomTitle("테스트" + i)
                .thumbnail("test" + i + ".png")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(2))
                .build();

            reservations.add(item);
        }

        PagedDto<ReserveListItemDto> pagedDto = PagedDto.<ReserveListItemDto>builder()
            .totalElements(5)
            .totalPages(1)
            .size(5)
            .currentPage(1)
            .content(reservations)
            .build();

        when(reserveService.getList(1L, 1, 5, "asc")).thenReturn(pagedDto);

        mvc.perform(
                get("/api/reservation?page=1&size=5&direction=asc")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("userId", "1")
                    .param("page", "1")
                    .param("size", "5")
                    .param("sort", "asc")
                    .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.resultCode").value("200"))
            .andExpect(jsonPath("$.resultMessage").value("OK"))
            .andExpect(jsonPath("$.data.totalElements").value("5"))
            .andExpect(jsonPath("$.data.totalPages").value("1"))
            .andExpect(jsonPath("$.data.currentPage").value("1"))
            .andExpect(jsonPath("$.data.content").isArray())
            .andExpect(jsonPath("$.data.content").isNotEmpty());
    }


    @Test
    @DisplayName("예약 하기")
    public void reserve() throws Exception {
        PostReserveRequestDto body = new PostReserveRequestDto(4, LocalDate.now().plusDays(1),
            LocalDate.now().plusDays(2), 40000);

        ReserveResponseDto response = ReserveResponseDto.builder()
            .userId(user.getUserId())
            .headcount(4)
            .price(40000)
            .startDate(LocalDate.now().plusDays(1))
            .endDate(LocalDate.now().plusDays(2))
            .accommodationId(1L)
            .roomId(1L)
            .createdAt(Instant.now())
            .build();

        when(reserveService.postReserve(user.getUserId(), 1L, 1L, body))
            .thenReturn(response);

        mvc.perform(post("/api/reservation/{accommodationId}/room/{roomId}/reserve", 1, 1)
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + token)
            .content(objectMapper.writeValueAsString(body)
            )).andExpect(status().isCreated());
    }


    @Test
    @DisplayName("예약 취소하기")
    public void cancelReservation() throws Exception{
        ReserveResponseDto response = ReserveResponseDto.builder()
            .userId(user.getUserId())
            .headcount(4)
            .price(40000)
            .startDate(LocalDate.now().plusDays(1))
            .endDate(LocalDate.now().plusDays(2))
            .accommodationId(1L)
            .roomId(1L)
            .createdAt(Instant.now().minusSeconds(60))
            .deletedAt(Instant.now())
            .build();


        when(reserveService.cancelReserve(anyLong())).thenReturn(response);

        mvc.perform(put("/api/reservation/{reservationId}", 1)
            .with(csrf())
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.resultCode").value("200"))
            .andExpect(jsonPath("$.resultMessage").value("OK"))
            .andExpect(jsonPath("$.data.deletedAt").isNotEmpty());

    }
}