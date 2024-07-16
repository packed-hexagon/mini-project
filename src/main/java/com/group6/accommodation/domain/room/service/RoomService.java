package com.group6.accommodation.domain.room.service;

import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.accommodation.repository.AccommodationRepository;
import com.group6.accommodation.domain.reservation.repository.ReservationRepository;
import com.group6.accommodation.domain.room.model.dto.AvailableRoomsReq;
import com.group6.accommodation.domain.room.model.dto.AvailableRoomsRes;
import com.group6.accommodation.domain.room.model.dto.RoomDto;
import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import com.group6.accommodation.domain.room.repository.RoomRepository;
import com.group6.accommodation.global.exception.error.RoomErrorCode;
import com.group6.accommodation.global.exception.type.RoomException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomService {

	private final RoomRepository roomRepository;
	private final AccommodationRepository accommodationRepository;
	private final ReservationRepository reservationRepository;

	@Transactional
	public void saveRooms(List<RoomEntity> rooms) {
		for (RoomEntity room : rooms) {

			Optional<RoomEntity> existingRoom = roomRepository.findById(room.getRoomId());

			if (existingRoom.isPresent()) {
				RoomEntity existing = existingRoom.get();
				AccommodationEntity accommodation = accommodationRepository.findById(
					existing.getAccommodation().getId()).orElseThrow();

				existing.updateRoomEntity(accommodation, room);

				roomRepository.save(existing);
			} else {
				roomRepository.save(room);
			}
		}
	}

	public List<RoomDto> findByAccommodationId(Long accommodationId) {

		// 숙소 검증
		List<RoomEntity> roomEntityList = roomRepository.getByAccommodation_Id(accommodationId);

		return RoomDto.toDtoList(roomEntityList);
	}

	public RoomDto findByAccommodationIdAndRoomId(Long accommodationId, Long roomId) {

		// 숙소 검증
		List<RoomEntity> roomEntityList = roomRepository.getByAccommodation_Id(accommodationId);

		// 객실 검증
		RoomEntity roomEntity = roomRepository.getByAccommodation_IdAndRoomId(accommodationId, roomId);

		return RoomDto.toDto(roomEntity);
	}

	// 룸 카운트 /
	public AvailableRoomsRes availableRooms(AvailableRoomsReq req, Long id, Long roomId) {

		// 숙소 검증
		List<RoomEntity> roomEntityList = roomRepository.getByAccommodation_Id(id);

		// 객실 검증
		RoomEntity roomEntity = roomRepository.getByAccommodation_IdAndRoomId(id, roomId);

		int roomCount = roomEntity.getCount();
		int reservationRooms = reservationRepository.countOverlappingReservations(
			roomId, req.getCheckIn(), req.getCheckOut()
		);

		return roomCount > reservationRooms ? AvailableRoomsRes.builder().reservable(true).build()
			: AvailableRoomsRes.builder().reservable(false).build();
	}
}