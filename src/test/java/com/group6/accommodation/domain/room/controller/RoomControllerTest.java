package com.group6.accommodation.domain.room.controller;

import com.group6.accommodation.domain.room.model.dto.RoomDto;
import com.group6.accommodation.domain.room.service.RoomService;
import com.group6.accommodation.global.exception.error.RoomErrorCode;
import com.group6.accommodation.global.exception.type.RoomException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RoomController.class)
public class RoomControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RoomService roomService;

	@Test
	public void 숙박Id로객실조회_객실이있을때() throws Exception {
		Long accommodationId = 136039L;
		RoomDto roomDto = new RoomDto();
		List<RoomDto> roomDtoList = Collections.singletonList(roomDto);

		given(roomService.findByAccommodationId(accommodationId)).willReturn(roomDtoList);

		mockMvc.perform(get("/open-api/accommodation/{id}/room", accommodationId))
			.andExpect(status().isOk())
			.andExpect(content().json("{\"status\":\"OK\",\"message\":\"\",\"data\":[{}]}"));
	}

	@Test
	public void 숙박Id로객실조회_객실이없을때() throws Exception {
		Long nonExistentAccommodationId = 999L;

		given(roomService.findByAccommodationId(nonExistentAccommodationId)).willThrow(
			new RoomException(RoomErrorCode.NOT_FOUND_ACCOMMODATION)
		);

		mockMvc.perform(get("/open-api/accommodation/{id}/room", nonExistentAccommodationId))
			.andExpect(status().isNotFound())
			.andExpect(content().json("{\"status\":\"NOT_FOUND\",\"message\":\"없는 숙박 ID 입니다.\",\"data\":null}"));
	}

	@Test
	public void 숙박Id와객실Id로객실조회_객실있을때() throws Exception {
		Long accommodationId = 136039L;
		Long roomId = 30L;
		RoomDto roomDto = new RoomDto();

		given(roomService.findByAccommodationIdAndRoomId(accommodationId, roomId)).willReturn(roomDto);

		mockMvc.perform(get("/open-api/accommodation/{id}/room/{roomId}", accommodationId, roomId))
			.andExpect(status().isOk())
			.andExpect(content().json("{\"status\":\"OK\",\"message\":\"\",\"data\":{}}"));
	}

	@Test
	public void 숙박Id와객실Id로객실조회_객실없을때() throws Exception {
		Long nonExistentAccommodationId = 999L;
		Long nonExistentRoomId = 999L;

		given(roomService.findByAccommodationIdAndRoomId(nonExistentAccommodationId, nonExistentRoomId)).willThrow(
			new RoomException(RoomErrorCode.NOT_FOUND_ROOM)
		);

		mockMvc.perform(get("/open-api/accommodation/{id}/room/{roomId}", nonExistentAccommodationId, nonExistentRoomId))
			.andExpect(status().isNotFound())
			.andExpect(content().json("{\"status\":\"NOT_FOUND\",\"message\":\"없는 객실 ID 입니다.\",\"data\":null}"));
	}

}
