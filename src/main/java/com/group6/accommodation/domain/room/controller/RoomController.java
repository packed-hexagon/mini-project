package com.group6.accommodation.domain.room.controller;

import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import com.group6.accommodation.domain.room.service.RoomService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("/open-api")
@RequiredArgsConstructor
public class RoomController {

	private final RoomService roomService;

	@GetMapping("/accommodation/{id}/room")
	public List<RoomEntity> findAllRooms(@PathVariable Long id) {
		return roomService.findAllRooms(id);
	}
}
