package com.group6.accommodation.domain.room.model.entity;

import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
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

	@Column(name = "room_size", nullable = false)
	private Integer roomSize;

	@Column(name = "room_count", nullable = false)
	private Integer roomCount;

	@Column(name = "room_base_count", nullable = false)
	private Integer roomBaseCount;

	@Column(name = "room_max_count", nullable = false)
	private Integer roomMaxCount;

	@Column(name = "room_offseason_minfee1")
	private Integer roomOffseasonMinfee1;

	@Column(name = "room_offseason_minfee2")
	private Integer roomOffseasonMinfee2;

	@Column(name = "room_peakseason_minfee1")
	private Integer roomPeakseasonMinfee1;

	@Column(name = "room_peakseason_minfee2")
	private Integer roomPeakseasonMinfee2;

	@Lob
	@Column(name = "room_intro", columnDefinition = "TEXT")
	private String roomIntro;

	@Column(name = "room_bath", length = 32)
	private String roomBath;

	@Column(name = "room_hometheater", length = 32)
	private String roomHometheater;

	@Column(name = "room_aircondition", length = 32)
	private String roomAircondition;

	@Column(name = "room_tv", length = 32)
	private String roomTv;

	@Column(name = "room_pc", length = 32)
	private String roomPc;

	@Column(name = "room_cable", length = 32)
	private String roomCable;

	@Column(name = "room_internet", length = 32)
	private String roomInternet;

	@Column(name = "room_refrigerator", length = 32)
	private String roomRefrigerator;

	@Column(name = "room_toiletries", length = 32)
	private String roomToiletries;

	@Column(name = "room_sofa", length = 32)
	private String roomSofa;

	@Column(name = "room_cook", length = 32)
	private String roomCook;

	@Column(name = "room_table", length = 32)
	private String roomTable;

	@Column(name = "room_hairdryer", length = 32)
	private String roomHairdryer;

	@Column(name = "room_img1", length = 300)
	private String roomImg1;

	@Column(name = "room_img2", length = 300)
	private String roomImg2;

	@Column(name = "room_img3", length = 300)
	private String roomImg3;

	@Column(name = "room_img4", length = 300)
	private String roomImg4;

	@Column(name = "room_img5", length = 300)
	private String roomImg5;

	@Column(name = "check_in")
	private Instant checkIn;

	@Column(name = "check_out")
	private Instant checkOut;

	public void updateRoomEntity(AccommodationEntity accommodation, RoomEntity entity) {
		this.accommodation = accommodation;
		this.roomTitle = entity.getRoomTitle();
		this.roomSize = entity.getRoomSize();
		this.roomCount = entity.getRoomCount();
		this.roomBaseCount = entity.getRoomBaseCount();
		this.roomMaxCount = entity.getRoomMaxCount();
		this.roomOffseasonMinfee1 = entity.getRoomOffseasonMinfee1();
		this.roomOffseasonMinfee2 = entity.getRoomOffseasonMinfee2();
		this.roomPeakseasonMinfee1 = entity.getRoomPeakseasonMinfee1();
		this.roomPeakseasonMinfee2 = entity.getRoomPeakseasonMinfee2();
		this.roomIntro = entity.getRoomIntro();
		this.roomBath = entity.getRoomBath();
		this.roomHometheater = entity.getRoomHometheater();
		this.roomAircondition = entity.getRoomAircondition();
		this.roomTv = entity.getRoomTv();
		this.roomPc = entity.getRoomPc();
		this.roomCable = entity.getRoomCable();
		this.roomInternet = entity.getRoomInternet();
		this.roomRefrigerator = entity.getRoomRefrigerator();
		this.roomToiletries = entity.getRoomToiletries();
		this.roomSofa = entity.getRoomSofa();
		this.roomCook = entity.getRoomCook();
		this.roomTable = entity.getRoomTable();
		this.roomHairdryer = entity.getRoomHairdryer();
		this.roomImg1 = entity.getRoomImg1();
		this.roomImg2 = entity.getRoomImg2();
		this.roomImg3 = entity.getRoomImg3();
		this.roomImg4 = entity.getRoomImg4();
		this.roomImg5 = entity.getRoomImg5();
	}

	@Builder
	public RoomEntity(Long id, AccommodationEntity accommodation, String roomTitle, Integer roomSize, Integer roomCount, Integer roomBaseCount, Integer roomMaxCount, Integer roomOffseasonMinfee1, Integer roomOffseasonMinfee2, Integer roomPeakseasonMinfee1, Integer roomPeakseasonMinfee2, String roomIntro, String roomBath, String roomHometheater, String roomAircondition, String roomTv, String roomPc, String roomCable, String roomInternet, String roomRefrigerator, String roomToiletries, String roomSofa, String roomCook, String roomTable, String roomHairdryer, String roomImg1, String roomImg2, String roomImg3, String roomImg4, String roomImg5) {
		this.roomId = id;
		this.accommodation = accommodation;
		this.roomTitle = roomTitle;
		this.roomSize = roomSize;
		this.roomCount = roomCount;
		this.roomBaseCount = roomBaseCount;
		this.roomMaxCount = roomMaxCount;
		this.roomOffseasonMinfee1 = roomOffseasonMinfee1;
		this.roomOffseasonMinfee2 = roomOffseasonMinfee2;
		this.roomPeakseasonMinfee1 = roomPeakseasonMinfee1;
		this.roomPeakseasonMinfee2 = roomPeakseasonMinfee2;
		this.roomIntro = roomIntro;
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
		this.roomImg1 = roomImg1;
		this.roomImg2 = roomImg2;
		this.roomImg3 = roomImg3;
		this.roomImg4 = roomImg4;
		this.roomImg5 = roomImg5;
	}

}
