package com.group6.accommodation.domain.room.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import com.group6.accommodation.domain.room.repository.RoomRepository;
import java.net.URISyntaxException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiProcessRoomService {

	private final RoomApiService roomApiService;
	private final RoomService roomService;
	private final RoomRepository roomRepository;

	public void processRooms() throws URISyntaxException, JsonProcessingException {
		List<RoomEntity> rooms = roomApiService.fetchAllRooms();
		roomService.saveRooms(rooms);
	}

	public boolean isDatabaseEmpty() {
		return roomRepository.count() == 0;
	}
}
