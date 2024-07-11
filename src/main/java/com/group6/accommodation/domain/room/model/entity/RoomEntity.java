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

	@Column(name = "room_title", nullable = false, length = 32)
	private String roomTitle;

	@Column(name = "room_count", nullable = false)
	private Integer count;

	@Column(name = "room_base_count", nullable = false)
	private Integer baseCount;

	@Column(name = "room_max_count", nullable = false)
	private Integer maxHeadCount;

	@Column(name = "room_offseason_minfee1", nullable = false)
	private Integer weekdaysFee;

	@Column(name = "room_offseason_minfee2", nullable = false)
	private Integer weekendsFee;

	@Column(name = "room_bath", length = 32, nullable = false)
	private boolean roomBath;

	@Column(name = "room_hometheater", length = 32, nullable = false)
	private boolean roomHometheater;

	@Column(name = "room_aircondition", length = 32, nullable = false)
	private boolean roomAircondition;

	@Column(name = "room_tv", length = 32, nullable = false)
	private boolean roomTv;

	@Column(name = "room_pc", length = 32, nullable = false)
	private boolean roomPc;

	@Column(name = "room_cable", length = 32, nullable = false)
	private boolean roomCable;

	@Column(name = "room_internet", length = 32, nullable = false)
	private boolean roomInternet;

	@Column(name = "room_refrigerator", length = 32, nullable = false)
	private boolean roomRefrigerator;

	@Column(name = "room_toiletries", length = 32, nullable = false)
	private boolean roomToiletries;

	@Column(name = "room_sofa", length = 32, nullable = false)
	private boolean roomSofa;

	@Column(name = "room_cook", length = 32, nullable = false)
	private boolean roomCook;

	@Column(name = "room_table", length = 32, nullable = false)
	private boolean roomTable;

	@Column(name = "room_hairdryer", length = 32, nullable = false)
	private boolean roomHairdryer;

	@Column(name = "images", length = 500, nullable = false)
	private String images;

	@Column(name = "check_in_time", nullable = false)
	private LocalDate checkInTime;

	@Column(name = "check_out_time", nullable = false)
	private LocalDate checkOutTime;

	public void updateRoomEntity(AccommodationEntity accommodation, RoomEntity entity) {
		this.accommodation = accommodation;
		this.roomTitle = entity.getRoomTitle();
		this.count = entity.getCount();
		this.baseCount = entity.getBaseCount();
		this.maxHeadCount = entity.getMaxHeadCount();
		this.weekdaysFee = entity.getWeekdaysFee();
		this.weekendsFee = entity.getWeekendsFee();
		this.roomBath = entity.isRoomBath();
		this.roomHometheater = entity.isRoomHometheater();
		this.roomAircondition = entity.isRoomAircondition();
		this.roomTv = entity.isRoomTv();
		this.roomPc = entity.isRoomPc();
		this.roomCable = entity.isRoomCable();
		this.roomInternet = entity.isRoomInternet();
		this.roomRefrigerator = entity.isRoomRefrigerator();
		this.roomToiletries = entity.isRoomToiletries();
		this.roomSofa = entity.isRoomSofa();
		this.roomCook = entity.isRoomCook();
		this.roomTable = entity.isRoomTable();
		this.roomHairdryer = entity.isRoomHairdryer();
		this.images = entity.getImages();
		this.checkInTime = entity.getCheckInTime();
		this.checkOutTime = entity.getCheckOutTime();
	}

	@Builder
	public RoomEntity(Long id, AccommodationEntity accommodation, String roomTitle, Integer count, Integer baseCount, Integer maxHeadCount, Integer weekdaysFee, Integer weekendsFee, boolean roomBath, boolean roomHometheater, boolean roomAircondition, boolean roomTv, boolean roomPc, boolean roomCable, boolean roomInternet, boolean roomRefrigerator, boolean roomToiletries, boolean roomSofa, boolean roomCook, boolean roomTable, boolean roomHairdryer, String images, LocalDate checkInTime, LocalDate checkOutTime) {
		this.roomId = id;
		this.accommodation = accommodation;
		this.roomTitle = roomTitle;
		this.count = count;
		this.baseCount = baseCount;
		this.maxHeadCount = maxHeadCount;
		this.weekdaysFee = weekdaysFee;
		this.weekendsFee = weekendsFee;
		this.roomBath = roomBath;
		this.roomHometheater = roomHometheater;
		this.roomAircondition = roomAircondition;
		this.roomTv = roomTv;
		this.roomPc = roomPc;
		this.roomCable = roomCable;
		this.roomInternet = roomInternet;
		this.roomRefrigerator = roomRefrigerator;
		this.roomToiletries = roomToiletries;
		this.roomSofa = roomSofa;
		this.roomCook = roomCook;
		this.roomTable = roomTable;
		this.roomHairdryer = roomHairdryer;
		this.images = images;
		this.checkInTime = checkInTime;
		this.checkOutTime = checkOutTime;
	}

}
