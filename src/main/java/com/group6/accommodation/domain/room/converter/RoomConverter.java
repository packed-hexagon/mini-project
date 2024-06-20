package com.group6.accommodation.domain.room.converter;


import com.group6.accommodation.domain.room.model.dto.RoomDto;
import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomConverter {

	public RoomDto toDto(RoomEntity room) {

		return RoomDto.builder()
			.roomId(room.getRoomId())
			.accommodationId(room.getAccommodation().getId())
			.roomTitle(room.getRoomTitle())
			.roomSize(room.getRoomSize())
			.roomBaseCount(room.getRoomBaseCount())
			.roomMaxCount(room.getRoomMaxCount())
			.roomOffseasonMinfee1(room.getRoomOffseasonMinfee1())
			.roomOffseasonMinfee2(room.getRoomOffseasonMinfee2())
			.roomPeakseasonMinfee1(room.getRoomPeakseasonMinfee1())
			.roomPeakseasonMinfee2(room.getRoomPeakseasonMinfee2())
			.roomIntro(room.getRoomIntro())
			.roomBath(room.getRoomBath())
			.roomHometheater(room.getRoomHometheater())
			.roomAircondition(room.getRoomAircondition())
			.roomTv(room.getRoomTv())
			.roomPc(room.getRoomPc())
			.roomCable(room.getRoomCable())
			.roomInternet(room.getRoomInternet())
			.roomRefrigerator(room.getRoomRefrigerator())
			.roomToiletries(room.getRoomToiletries())
			.roomSofa(room.getRoomSofa())
			.roomCook(room.getRoomCook())
			.roomTable(room.getRoomTable())
			.roomHairdryer(room.getRoomHairdryer())
			.roomImg1(room.getRoomImg1())
			.roomImg2(room.getRoomImg2())
			.roomImg3(room.getRoomImg3())
			.roomImg4(room.getRoomImg4())
			.roomImg5(room.getRoomImg5())
			.checkIn(room.getCheckIn())
			.checkOut(room.getCheckOut())
			.build();
	}

	public List<RoomDto> toDtoList(List<RoomEntity> roomEntityList) {
		return roomEntityList.stream().map(this::toDto).collect(Collectors.toList());
	}
}
