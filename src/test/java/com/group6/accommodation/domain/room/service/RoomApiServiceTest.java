package com.group6.accommodation.domain.room.service;

import static org.junit.jupiter.api.Assertions.*;

import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RoomApiServiceTest {

	@Autowired
	RoomApiService roomApiService;

	@Autowired
	RoomService roomService;

	@Test
	public void asd() {
		List<RoomEntity> rooms = roomApiService.fetchAllRooms();
//		List<RoomEntity> roomEntityList = rooms.join().stream().toList();
		roomService.saveRooms(rooms);
	}
}