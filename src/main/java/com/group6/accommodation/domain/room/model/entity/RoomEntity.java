package com.group6.accommodation.domain.room.model.entity;

import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "room")
public class RoomEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "room_id", nullable = false)
	private Long roomId;

	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name="accommodation_id", referencedColumnName = "accommodation_id")
	private AccommodationEntity accommodation;

	@Column(name = "title", nullable = false, length = 32)
	private String title;

	@Column(name = "count", nullable = false)
	private Integer count;

	@Column(name = "base_count", nullable = false)
	private Integer baseCount;

	@Column(name = "max_head_count", nullable = false)
	private Integer maxHeadCount;

	@Column(name = "week_days_fee", nullable = false)
	private Integer weekdaysFee;

	@Column(name = "weekends_fee", nullable = false)
	private Integer weekendsFee;

	@Column(name = "bath", nullable = false)
	private boolean bath;

	@Column(name = "hometheater", nullable = false)
	private boolean hometheater;

	@Column(name = "aircondition", nullable = false)
	private boolean aircondition;

	@Column(name = "tv", nullable = false)
	private boolean tv;

	@Column(name = "pc", nullable = false)
	private boolean pc;

	@Column(name = "cable", nullable = false)
	private boolean cable;

	@Column(name = "internet", nullable = false)
	private boolean internet;

	@Column(name = "refrigerator", nullable = false)
	private boolean refrigerator;

	@Column(name = "toiletries", nullable = false)
	private boolean toiletries;

	@Column(name = "sofa", nullable = false)
	private boolean sofa;

	@Column(name = "cook", nullable = false)
	private boolean cook;

	@Column(name = "table", nullable = false)
	private boolean table;

	@Column(name = "hairdryer", nullable = false)
	private boolean hairdryer;

	@Column(name = "images", length = 500, nullable = false)
	private String images;

	@Column(name = "check_in_time", nullable = false)
	private LocalDateTime checkInTime;

	@Column(name = "check_out_time", nullable = false)
	private LocalDateTime checkOutTime;

	public void updateRoomEntity(AccommodationEntity accommodation, RoomEntity entity) {
		this.accommodation = accommodation;
		this.title = entity.getTitle();
		this.count = entity.getCount();
		this.baseCount = entity.getBaseCount();
		this.maxHeadCount = entity.getMaxHeadCount();
		this.weekdaysFee = entity.getWeekdaysFee();
		this.weekendsFee = entity.getWeekendsFee();
		this.bath = entity.isBath();
		this.hometheater = entity.isHometheater();
		this.aircondition = entity.isAircondition();
		this.tv = entity.isTv();
		this.pc = entity.isPc();
		this.cable = entity.isCable();
		this.internet = entity.isInternet();
		this.refrigerator = entity.isRefrigerator();
		this.toiletries = entity.isToiletries();
		this.sofa = entity.isSofa();
		this.cook = entity.isCook();
		this.table = entity.isTable();
		this.hairdryer = entity.isHairdryer();
		this.images = entity.getImages();
		this.checkInTime = entity.getCheckInTime();
		this.checkOutTime = entity.getCheckOutTime();
	}

	@Builder
	public RoomEntity(Long id, AccommodationEntity accommodation, String title, Integer count, Integer baseCount, Integer maxHeadCount, Integer weekdaysFee, Integer weekendsFee, boolean bath, boolean hometheater, boolean aircondition, boolean tv, boolean pc, boolean cable, boolean internet, boolean refrigerator, boolean toiletries, boolean sofa, boolean cook, boolean table, boolean hairdryer, String images, LocalDateTime checkInTime, LocalDateTime checkOutTime) {
		this.roomId = id;
		this.accommodation = accommodation;
		this.title = title;
		this.count = count;
		this.baseCount = baseCount;
		this.maxHeadCount = maxHeadCount;
		this.weekdaysFee = weekdaysFee;
		this.weekendsFee = weekendsFee;
		this.bath = bath;
		this.hometheater = hometheater;
		this.aircondition = aircondition;
		this.tv = tv;
		this.pc = pc;
		this.cable = cable;
		this.internet = internet;
		this.refrigerator = refrigerator;
		this.toiletries = toiletries;
		this.sofa = sofa;
		this.cook = cook;
		this.table = table;
		this.hairdryer = hairdryer;
		this.images = images;
		this.checkInTime = checkInTime;
		this.checkOutTime = checkOutTime;
	}

}
