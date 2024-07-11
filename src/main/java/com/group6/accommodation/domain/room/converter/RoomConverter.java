package com.group6.accommodation.domain.room.converter;


import com.group6.accommodation.domain.room.model.dto.RoomDto;
import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import java.util.List;
import java.util.stream.Collectors;

public class RoomConverter {

	public static RoomDto toDto(RoomEntity room) {

		return RoomDto.builder()
			.roomId(room.getRoomId())
			.accommodationId(room.getAccommodation().getId())
			.roomTitle(room.getRoomTitle())
			.baseCount(room.getBaseCount())
			.maxHeadCount(room.getMaxHeadCount())
			.weekdaysFee(room.getWeekdaysFee())
			.weekendsFee(room.getWeekendsFee())
			.roomBath(room.isRoomBath())
			.roomHometheater(room.isRoomHometheater())
			.roomAircondition(room.isRoomAircondition())
			.roomTv(room.isRoomTv())
			.roomPc(room.isRoomPc())
			.roomCable(room.isRoomCable())
			.roomInternet(room.isRoomInternet())
			.roomRefrigerator(room.isRoomRefrigerator())
			.roomToiletries(room.isRoomToiletries())
			.roomSofa(room.isRoomSofa())
			.roomCook(room.isRoomCook())
			.roomTable(room.isRoomTable())
			.roomHairdryer(room.isRoomHairdryer())
			.images(room.getImages())
			.checkIn(room.getCheckInTime())
			.checkOut(room.getCheckOutTime())
			.build();
	}

	public static List<RoomDto> toDtoList(List<RoomEntity> roomEntityList) {
		return roomEntityList.stream().map(RoomConverter::toDto).collect(Collectors.toList());
	}
}
