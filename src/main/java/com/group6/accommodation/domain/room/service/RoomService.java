package com.group6.accommodation.domain.room.service;

import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.accommodation.repository.AccommodationRepository;
import com.group6.accommodation.domain.reservation.model.entity.ReservationEntity;
import com.group6.accommodation.domain.reservation.repository.ReservationRepository;
import com.group6.accommodation.domain.room.converter.RoomConverter;
import com.group6.accommodation.domain.room.model.dto.AvailableRoomsReq;
import com.group6.accommodation.domain.room.model.dto.AvailableRoomsRes;
import com.group6.accommodation.domain.room.model.dto.RoomDto;
import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import com.group6.accommodation.domain.room.repository.RoomRepository;
import com.group6.accommodation.global.exception.error.RoomErrorCode;
import com.group6.accommodation.global.exception.type.RoomException;
import com.group6.accommodation.global.util.ResponseApi;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {

	private final RoomRepository roomRepository;
	private final RoomConverter roomConverter;
	private final AccommodationRepository accommodationRepository;
	private final ReservationRepository reservationRepository;

	public void saveRooms(List<RoomEntity> rooms) {
		for (RoomEntity room : rooms) {
			Optional<RoomEntity> existingRoom = roomRepository.findById(room.getRoomId());
			if (existingRoom.isPresent()) {
				RoomEntity existing = existingRoom.get();
				AccommodationEntity accommodation = accommodationRepository.findById(
					existing.getAccommodation().getId()).orElseThrow();
				existing.setAccommodation(accommodation);
				existing.setRoomTitle(room.getRoomTitle());
				existing.setRoomSize(room.getRoomSize());
				existing.setRoomCount(room.getRoomCount());
				existing.setRoomBaseCount(room.getRoomBaseCount());
				existing.setRoomMaxCount(room.getRoomMaxCount());
				existing.setRoomOffseasonMinfee1(room.getRoomOffseasonMinfee1());
				existing.setRoomOffseasonMinfee2(room.getRoomOffseasonMinfee2());
				existing.setRoomPeakseasonMinfee1(room.getRoomPeakseasonMinfee1());
				existing.setRoomPeakseasonMinfee2(room.getRoomPeakseasonMinfee2());
				existing.setRoomIntro(room.getRoomIntro());
				existing.setRoomBath(room.getRoomBath());
				existing.setRoomHometheater(room.getRoomHometheater());
				existing.setRoomAircondition(room.getRoomAircondition());
				existing.setRoomTv(room.getRoomTv());
				existing.setRoomPc(room.getRoomPc());
				existing.setRoomCable(room.getRoomCable());
				existing.setRoomInternet(room.getRoomInternet());
				existing.setRoomRefrigerator(room.getRoomRefrigerator());
				existing.setRoomToiletries(room.getRoomToiletries());
				existing.setRoomSofa(room.getRoomSofa());
				existing.setRoomCook(room.getRoomCook());
				existing.setRoomTable(room.getRoomTable());
				existing.setRoomHairdryer(room.getRoomHairdryer());
				existing.setRoomImg1(room.getRoomImg1());
				existing.setRoomImg2(room.getRoomImg2());
				existing.setRoomImg3(room.getRoomImg3());
				existing.setRoomImg4(room.getRoomImg4());
				existing.setRoomImg5(room.getRoomImg5());
				existing.setCheckIn(room.getCheckIn());
				existing.setCheckOut(room.getCheckOut());

				roomRepository.save(existing);
			} else {
				roomRepository.save(room);
			}
		}
	}

	public ResponseApi<List<RoomDto>> findByAccommodationId(Long accommodationId) {

		// 숙소 검증
		List<RoomEntity> roomEntityList = roomRepository.findByAccommodation_Id(accommodationId);
		if (roomEntityList.isEmpty()) {
			throw new RoomException(RoomErrorCode.NOT_FOUND_ACCOMMODATION);
		}

		List<RoomDto> roomDtoList = roomConverter.toDtoList(roomEntityList);
		return ResponseApi.success(HttpStatus.OK, roomDtoList);
	}

	public ResponseApi<RoomDto> findByAccommodationIdAndRoomId(Long accommodationId, Long roomId) {

		// 숙소 검증
		List<RoomEntity> roomEntityList = roomRepository.findByAccommodation_Id(accommodationId);
		if (roomEntityList.isEmpty()) {
			throw new RoomException(RoomErrorCode.NOT_FOUND_ACCOMMODATION);
		}

		// 객실 검증
		RoomEntity roomEntity = roomRepository.findByAccommodation_IdAndRoomId(accommodationId, roomId)
			.orElseThrow(() -> new RoomException(RoomErrorCode.NOT_FOUND_ROOM));

		RoomDto roomDto = roomConverter.toDto(roomEntity);
		return ResponseApi.success(HttpStatus.OK, roomDto);
	}

	public AvailableRoomsRes availableRooms(AvailableRoomsReq req, Long accommodationId, Long roomId) {

		// 숙소 검증
		List<RoomEntity> roomEntityList = roomRepository.findByAccommodation_Id(accommodationId);
		if (roomEntityList.isEmpty()) {
			throw new RoomException(RoomErrorCode.NOT_FOUND_ACCOMMODATION);
		}

		Optional<ReservationEntity> reservationEntity = reservationRepository.findByStartDateBeforeOrEndDateAfter(
			req.getCheckOut(), req.getCheckIn()
		);

		if (reservationEntity.isEmpty()) return AvailableRoomsRes.builder().isReservable(true).build();
		return AvailableRoomsRes.builder().isReservable(false).build();
	}
}
