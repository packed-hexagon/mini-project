package com.group6.accommodation.domain.room.controller;

import com.group6.accommodation.domain.room.model.dto.AvailableRoomsReq;
import com.group6.accommodation.domain.room.model.dto.AvailableRoomsRes;
import com.group6.accommodation.domain.room.model.dto.RoomDto;
import com.group6.accommodation.domain.room.service.RoomService;
import com.group6.accommodation.global.util.ResponseApi;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/open-api")
@Tag(name = "Room", description = "객실 관련 API")
public class RoomController {

	private final RoomService roomService;

	@GetMapping("/accommodation/{id}/room")
	public ResponseEntity<ResponseApi<List<RoomDto>>> findByAccommodationId(
		@PathVariable Long id
	) {
		List<RoomDto> roomDtoList = roomService.findByAccommodationId(id);

		return ResponseEntity.ok(ResponseApi.success(HttpStatus.OK, roomDtoList));
	}

	@GetMapping("/accommodation/{id}/room/{roomId}")
	public ResponseEntity<ResponseApi<RoomDto>> findByAccommodationIdAndRoomId(
		@PathVariable Long id,
		@PathVariable Long roomId
	) {
		RoomDto roomDto = roomService.findByAccommodationIdAndRoomId(id, roomId);

		return ResponseEntity.ok(ResponseApi.success(HttpStatus.OK, roomDto));
	}

	@PostMapping("/accommodation/{id}/room/{roomId}/is-reservable")
	public ResponseEntity<ResponseApi<AvailableRoomsRes>> availableRooms(
		@PathVariable Long id,
		@PathVariable Long roomId,
		@RequestBody AvailableRoomsReq req
	) {
		AvailableRoomsRes isRoom = roomService.availableRooms(req, id, roomId);

		return ResponseEntity.ok(ResponseApi.success(HttpStatus.OK, isRoom));
	}
}
