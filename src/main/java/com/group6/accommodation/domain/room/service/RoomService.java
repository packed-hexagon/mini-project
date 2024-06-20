package com.group6.accommodation.domain.room.service;

import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import com.group6.accommodation.domain.room.repository.RoomRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {

	private final RoomRepository roomRepository;

	public List<RoomEntity> findAllRooms(Long accommodationId) {
		return roomRepository.findAll();
	}
}
