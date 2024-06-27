package com.group6.accommodation.domain.room.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import java.net.URISyntaxException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiProcessService {

	private final RoomApiService roomApiService;
	private final RoomService roomService;

	public void processRooms() throws URISyntaxException, JsonProcessingException {
		List<RoomEntity> rooms = roomApiService.fetchAllRooms();
		roomService.saveRooms(rooms);
	}
}
