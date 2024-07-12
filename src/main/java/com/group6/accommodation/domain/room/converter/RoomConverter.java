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
			.title(room.getTitle())
			.baseCount(room.getBaseCount())
			.maxHeadCount(room.getMaxHeadCount())
			.weekdaysFee(room.getWeekdaysFee())
			.weekendsFee(room.getWeekendsFee())
			.bath(room.isBath())
			.hometheater(room.isHometheater())
			.aircondition(room.isAircondition())
			.tv(room.isTv())
			.pc(room.isPc())
			.cable(room.isCable())
			.internet(room.isInternet())
			.refrigerator(room.isRefrigerator())
			.toiletries(room.isToiletries())
			.sofa(room.isSofa())
			.cook(room.isCook())
			.table(room.isTable())
			.hairdryer(room.isHairdryer())
			.images(room.getImages())
			.checkIn(room.getCheckInTime())
			.checkOut(room.getCheckOutTime())
			.build();
	}

	public static List<RoomDto> toDtoList(List<RoomEntity> roomEntityList) {
		return roomEntityList.stream().map(RoomConverter::toDto).collect(Collectors.toList());
	}
}
