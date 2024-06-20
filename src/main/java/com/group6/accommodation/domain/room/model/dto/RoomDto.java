package com.group6.accommodation.domain.room.model.dto;

import java.time.Instant;
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
	private Integer roomSize;
	private Integer roomCount;
	private Integer roomBaseCount;
	private Integer roomMaxCount;
	private Integer roomOffseasonMinfee1;
	private Integer roomOffseasonMinfee2;
	private Integer roomPeakseasonMinfee1;
	private Integer roomPeakseasonMinfee2;
	private String roomIntro;
	private String roomBath;
	private String roomHometheater;
	private String roomAircondition;
	private String roomTv;
	private String roomPc;
	private String roomCable;
	private String roomInternet;
	private String roomRefrigerator;
	private String roomToiletries;
	private String roomSofa;
	private String roomCook;
	private String roomTable;
	private String roomHairdryer;
	private String roomImg1;
	private String roomImg2;
	private String roomImg3;
	private String roomImg4;
	private String roomImg5;
	private Instant checkIn;
	private Instant checkOut;
}
