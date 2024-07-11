package com.group6.accommodation.domain.room.model.dto;

import java.time.LocalDate;
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
	private String roomTitle;
	private Integer count;
	private Integer baseCount;
	private Integer maxHeadCount;
	private Integer weekdaysFee;
	private Integer weekendsFee;
	private boolean roomBath;
	private boolean roomHometheater;
	private boolean roomAircondition;
	private boolean roomTv;
	private boolean roomPc;
	private boolean roomCable;
	private boolean roomInternet;
	private boolean roomRefrigerator;
	private boolean roomToiletries;
	private boolean roomSofa;
	private boolean roomCook;
	private boolean roomTable;
	private boolean roomHairdryer;
	private String images;
	private LocalDate checkIn;
	private LocalDate checkOut;
}
