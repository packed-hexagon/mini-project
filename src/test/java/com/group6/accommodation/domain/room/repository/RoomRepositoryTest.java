package com.group6.accommodation.domain.room.repository;

import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.accommodation.repository.AccommodationRepository;
import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class RoomRepositoryTest {

	@Autowired
	RoomRepository roomRepository;

	@Autowired
	AccommodationRepository accommodationRepository;

	@Test
	public void 숙박Id로객실조회_객실있을때() {
		// Given
		AccommodationEntity accommodation = AccommodationEntity.builder()
			.id(20L)
			.title("Sample Accommodation")
			.address("123 Main St")
			.address2("Apt 101")
			.areacode("12345")
			.sigungucode(123)
			.category("Hotel")
			.image("sample_image.jpg")
			.thumbnail("sample_thumbnail.jpg")
			.latitude(37.123456)
			.longitude(-122.654321)
			.mlevel(5)
			.tel("123-456-7890")
			.likeCount(100)
			.rating(4.5)
			.build();
		accommodationRepository.save(accommodation);

		RoomEntity room = RoomEntity.builder()
			.accommodation(accommodation)
			.roomTitle("Deluxe Room")
			.roomSize(30)
			.roomCount(2)
			.roomBaseCount(2)
			.roomMaxCount(4)
			.roomOffseasonMinfee1(100)
			.roomOffseasonMinfee2(120)
			.roomPeakseasonMinfee1(200)
			.roomPeakseasonMinfee2(220)
			.roomIntro("A beautiful deluxe room")
			.roomBath("Y")
			.roomHometheater("Y")
			.roomAircondition("Y")
			.roomTv("Y")
			.roomPc("Y")
			.roomCable("Y")
			.roomInternet("Y")
			.roomRefrigerator("Y")
			.roomToiletries("Y")
			.roomSofa("Y")
			.roomCook("Y")
			.roomTable("Y")
			.roomHairdryer("Y")
			.roomImg1("img1.jpg")
			.roomImg2("img2.jpg")
			.roomImg3("img3.jpg")
			.roomImg4("img4.jpg")
			.roomImg5("img5.jpg")
			.build();
		roomRepository.save(room);

		// When
		List<RoomEntity> rooms = roomRepository.findByAccommodation_Id(accommodation.getId());

		// Then
		assertFalse(rooms.isEmpty());
		assertEquals(1, rooms.size());
		assertEquals("Test Room", rooms.get(0).getRoomTitle());
	}

	@Test
	public void 숙박Id로객실조회_객실없을때() {
		Long nonExistentAccommodationId = 999L;

		List<RoomEntity> rooms = roomRepository.findByAccommodation_Id(nonExistentAccommodationId);

		assertTrue(rooms.isEmpty());
	}

	@Test
	public void 숙박Id와객실Id로객실조회_객실있을때() {
		// Given
		AccommodationEntity accommodation = AccommodationEntity.builder()
			.id(20L)
			.title("Sample Accommodation")
			.address("123 Main St")
			.address2("Apt 101")
			.areacode("12345")
			.sigungucode(123)
			.category("Hotel")
			.image("sample_image.jpg")
			.thumbnail("sample_thumbnail.jpg")
			.latitude(37.123456)
			.longitude(-122.654321)
			.mlevel(5)
			.tel("123-456-7890")
			.likeCount(100)
			.rating(4.5)
			.build();
		accommodationRepository.save(accommodation);

		RoomEntity room = RoomEntity.builder()
			.accommodation(accommodation)
			.roomTitle("Deluxe Room")
			.roomSize(30)
			.roomCount(2)
			.roomBaseCount(2)
			.roomMaxCount(4)
			.roomOffseasonMinfee1(100)
			.roomOffseasonMinfee2(120)
			.roomPeakseasonMinfee1(200)
			.roomPeakseasonMinfee2(220)
			.roomIntro("A beautiful deluxe room")
			.roomBath("Y")
			.roomHometheater("Y")
			.roomAircondition("Y")
			.roomTv("Y")
			.roomPc("Y")
			.roomCable("Y")
			.roomInternet("Y")
			.roomRefrigerator("Y")
			.roomToiletries("Y")
			.roomSofa("Y")
			.roomCook("Y")
			.roomTable("Y")
			.roomHairdryer("Y")
			.roomImg1("img1.jpg")
			.roomImg2("img2.jpg")
			.roomImg3("img3.jpg")
			.roomImg4("img4.jpg")
			.roomImg5("img5.jpg")
			.build();
		roomRepository.save(room);

		// When
		Optional<RoomEntity> foundRoom = roomRepository.findByAccommodation_IdAndRoomId(accommodation.getId(), room.getRoomId());

		// Then
		assertTrue(foundRoom.isPresent());
		assertEquals("Test Room", foundRoom.get().getRoomTitle());
	}

	@Test
	public void 숙박Id와객실Id로객실조회_객실없을때() {
		Long nonExistentAccommodationId = 999L;
		Long nonExistentRoomId = 999L;

		Optional<RoomEntity> room = roomRepository.findByAccommodation_IdAndRoomId(nonExistentAccommodationId, nonExistentRoomId);

		assertTrue(room.isEmpty());
	}
}
