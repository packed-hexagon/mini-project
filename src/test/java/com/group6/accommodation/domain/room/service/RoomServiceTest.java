package com.group6.accommodation.domain.room.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import com.group6.accommodation.domain.reservation.model.entity.ReservationEntity;
import com.group6.accommodation.domain.room.model.dto.RoomDto;
import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import com.group6.accommodation.domain.room.repository.RoomRepository;
import com.group6.accommodation.global.exception.type.RoomException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

	@InjectMocks
	private RoomService roomService;
	@Mock
	private RoomRepository roomRepository;

	private AccommodationEntity accommodation;
	private RoomEntity room;

	@BeforeEach
	void setUp() {
		accommodation = AccommodationEntity.builder()
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

		room = RoomEntity.builder()
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

	}

	@Test
	public void 숙박Id로객실조회_객실이있을때() {
		Long accommodationId = accommodation.getId();

		when(roomRepository.findByAccommodation_Id(accommodationId)).thenReturn(List.of(room));

		List<RoomDto> result = roomService.findByAccommodationId(accommodationId);

		assertNotNull(result);
		assertFalse(result.isEmpty());
		assertEquals(1, result.size());
		assertEquals(room.getRoomId(), result.get(0).getRoomId());
	}

	@Test
	public void 숙박Id로객실조회_객실이없을때() {
		Long accommodationId = accommodation.getId();

		// 객실이 없는 경우 설정
		when(roomRepository.findByAccommodation_Id(accommodationId)).thenReturn(Collections.emptyList());

		RoomException exception = assertThrows(RoomException.class, () -> {
			roomService.findByAccommodationId(accommodationId);
		});

		assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
	}

	@Test
	public void 숙박Id와객실Id로객실조회_객실이있을때() {
		Long roomId = room.getRoomId();
		Long accommodationId = accommodation.getId();

		when(roomRepository.findByAccommodation_Id(accommodationId)).thenReturn(List.of(room));
		when(roomRepository.findByAccommodation_IdAndRoomId(accommodationId, roomId)).thenReturn(Optional.of(room));

		RoomDto result = roomService.findByAccommodationIdAndRoomId(accommodationId, roomId);

		assertNotNull(result);
		assertEquals(accommodationId, result.getAccommodationId());
		assertEquals(roomId, result.getRoomId());
	}

	@Test
	public void 숙박Id와객실Id로객실조회_객실이없을때() {
		Long roomId = room.getRoomId();
		Long accommodationId = accommodation.getId();

		// 숙소는 존재하지만 객실이 없는 경우 설정
		when(roomRepository.findByAccommodation_Id(accommodationId)).thenReturn(List.of(room));
		when(roomRepository.findByAccommodation_IdAndRoomId(accommodationId, roomId)).thenReturn(Optional.empty());

		RoomException exception = assertThrows(RoomException.class, () -> {
			roomService.findByAccommodationIdAndRoomId(accommodationId, roomId);
		});

		assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
	}
}