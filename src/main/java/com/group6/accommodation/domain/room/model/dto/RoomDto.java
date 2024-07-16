package com.group6.accommodation.domain.room.model.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
}
