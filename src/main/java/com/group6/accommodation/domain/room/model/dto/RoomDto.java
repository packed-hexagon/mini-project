package com.group6.accommodation.domain.room.model.dto;

import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {

	private Long roomId;
	private Long accommodationId;
	private String title;
	private Integer count;
	private Integer baseCount;
	private Integer maxHeadCount;
	private Integer weekdaysFee;
	private Integer weekendsFee;
	private boolean bath;
	private boolean hometheater;
	private boolean aircondition;
	private boolean tv;
	private boolean pc;
	private boolean cable;
	private boolean internet;
	private boolean refrigerator;
	private boolean toiletries;
	private boolean sofa;
	private boolean cook;
	private boolean table;
	private boolean hairdryer;
	private String images;
	private LocalDateTime checkInTime;
	private LocalDateTime checkOutTime;

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
			.checkInTime(room.getCheckInTime())
			.checkOutTime(room.getCheckOutTime())
			.build();
	}

	public static List<RoomDto> toDtoList(List<RoomEntity> roomEntityList) {
		return roomEntityList.stream().map(RoomDto::toDto).collect(Collectors.toList());
	}
}
