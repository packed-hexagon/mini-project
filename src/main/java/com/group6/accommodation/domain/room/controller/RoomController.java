package com.group6.accommodation.domain.room.controller;

import com.group6.accommodation.domain.room.model.dto.AvailableRoomsReq;
import com.group6.accommodation.domain.room.model.dto.AvailableRoomsRes;
import com.group6.accommodation.domain.room.model.dto.RoomDto;
import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import com.group6.accommodation.domain.room.service.RoomService;
import com.group6.accommodation.global.util.Response;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/open-api")
public class RoomController {

	private final RoomService roomService;

	@GetMapping("/accommodation/{id}/room")
	public Response<List<RoomDto>> findByAccommodationId(
		@PathVariable Long id
	) {
		List<RoomDto> roomDtoList = roomService.findByAccommodationId(id);

		return Response.<List<RoomDto>>builder()
			.resultCode(String.valueOf(HttpStatus.OK.value()))
			.resultMessage(HttpStatus.OK.name())
			.data(roomDtoList)
			.build();
	}

	@GetMapping("/accommodation/{id}/room/{roomId}")
	public Response<RoomDto> findByAccommodationIdAndRoomId(
		@PathVariable Long id,
		@PathVariable Long roomId
	) {
		RoomDto roomDto = roomService.findByAccommodationIdAndRoomId(id, roomId);

		return Response.<RoomDto>builder()
			.resultCode(String.valueOf(HttpStatus.OK.value()))
			.resultCode(HttpStatus.OK.name())
			.data(roomDto)
			.build();
	}

	@GetMapping("/accommodation/{id}/room/{roomId}/is-reservable")
	public Response<AvailableRoomsRes> availableRooms(
		@PathVariable Long id,
		@PathVariable Long roomId,
		@RequestBody AvailableRoomsReq req
	) {
		AvailableRoomsRes availableRoomsRes = roomService.remainingRoom(req);
		return Response.<AvailableRoomsRes>builder()
			.resultCode(String.valueOf(HttpStatus.OK.value()))
			.resultCode(HttpStatus.OK.name())
			.data(availableRoomsRes)
			.build();
	}
}
