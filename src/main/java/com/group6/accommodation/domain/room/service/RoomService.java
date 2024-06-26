package com.group6.accommodation.domain.room.service;

import com.group6.accommodation.domain.room.converter.RoomConverter;
import com.group6.accommodation.domain.room.model.dto.AvailableRoomsReq;
import com.group6.accommodation.domain.room.model.dto.AvailableRoomsRes;
import com.group6.accommodation.domain.room.model.dto.RoomDto;
import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import com.group6.accommodation.domain.room.repository.RoomRepository;
import com.group6.accommodation.global.exception.error.RoomErrorCode;
import com.group6.accommodation.global.exception.type.RoomException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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
			throw new RoomException(RoomErrorCode.NOT_FOUND_ROOM);
		}
		return roomConverter.toDtoList(roomEntityList);
	}

	public RoomDto findByAccommodationIdAndRoomId(Long accommodationId, Long roomId) {

		RoomEntity roomEntity = roomRepository.findByAccommodation_IdAndRoomId(accommodationId, roomId)
			.orElseThrow(() -> new RoomException(RoomErrorCode.NOT_FOUND_ROOM));

		return roomConverter.toDto(roomEntity);
	}

	public AvailableRoomsRes remainingRoom(AvailableRoomsReq req) {
		List<RoomEntity> roomEntitityList = roomRepository.findAvailableRooms(
			req.getCheckIn(), req.getCheckOut()
		);
		if (roomEntitityList.isEmpty()) return AvailableRoomsRes.builder().isReservable(false).build();
		return AvailableRoomsRes.builder().isReservable(true).build();
	}
}
