package com.group6.accommodation.domain.room.service;

import com.group6.accommodation.domain.room.converter.RoomConverter;
import com.group6.accommodation.domain.room.model.dto.RoomDto;
import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import com.group6.accommodation.domain.room.repository.RoomRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {

	private final RoomRepository roomRepository;
	private final RoomConverter roomConverter;

	public List<RoomDto> findByAccommodationId(Long accommodationId) {

		List<RoomEntity> roomEntityList = roomRepository.findByAccommodation_Id(accommodationId);
		if (roomEntityList.isEmpty()) {
			throw new NoSuchElementException("No Room");
		}
		return roomConverter.toDtoList(roomEntityList);
	}

	public RoomDto findByAccommodationIdAndRoomId(Long accommodationId, Long roomId) {

		RoomEntity roomEntity = roomRepository.findByAccommodationIdAndRoomId(accommodationId,
			roomId);

		if (roomEntity == null) {
			throw new NoSuchElementException("No ID");
		}
		return roomConverter.toDto(roomEntity);
	}
}
