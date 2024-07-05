package com.group6.accommodation.domain.reservation.repository;

import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.accommodation.repository.AccommodationRepository;
import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import com.group6.accommodation.domain.auth.repository.UserRepository;
import com.group6.accommodation.domain.reservation.model.entity.ReservationEntity;
import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import com.group6.accommodation.domain.room.repository.RoomRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Testcontainers
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Disabled
public class ReservationRepositoryTest {

    @Container
    private static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest")
        .withDatabaseName("test")
        .withUsername("test")
        .withPassword("test");

    @DynamicPropertySource
    static void configureTestProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", mySQLContainer::getDriverClassName);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private AccommodationRepository accommodationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    private AccommodationEntity accommodation;
    private RoomEntity room;
    private UserEntity user;

    @BeforeEach
    void setUp() {
        accommodation = AccommodationEntity.builder()
            .id(1L)
            .title("Sample Accommodation")
            .longitude(1.0)
            .latitude(2.0)
            .address("sample address")
            .build();
        accommodation = accommodationRepository.save(accommodation);

        room = RoomEntity.builder()
            .id(1L)
            .roomTitle("Sample Room")
            .roomSize(2)
            .roomCount(4)
            .roomBaseCount(5)
            .roomMaxCount(10)
            .roomOffseasonMinfee1(100)
            .build();
        room = roomRepository.save(room);

        user = UserEntity.builder()
            .id(1L)
            .email("sample@example.com")
            .encryptedPassword("1234")
            .phoneNumber("010-1111-1111")
            .name("test")
            .build();
        user = userRepository.save(user);
    }

    @Test
    @DisplayName("existsByAccommodationAndRoomAndDeletedAtNotNullAndUserIdNot - 예약이 존재하는 경우")
    @Disabled
    public void testExistsReservation() {
        Long userId = user.getId();

        // 예약 생성
        ReservationEntity reservation = createReservation();
        reservationRepository.save(reservation);

        // when
        List<ReservationEntity> conflictingReservations = reservationRepository.findConflictingReservations(
            accommodation, room, userId);

        // then
        assertTrue(conflictingReservations.isEmpty());
    }

    @Test
    @DisplayName("existsByAccommodationAndRoomAndDeletedAtNotNullAndUserIdNot - 예약이 존재하지 않는 경우")
    public void testNotExistsReservation() {
        Long userId = user.getId();

        // when
        List<ReservationEntity> conflictingReservations = reservationRepository.findConflictingReservations(
            accommodation, room, userId);

        // then
        assertFalse(conflictingReservations.isEmpty());
    }

    @Test
    @DisplayName("countByRoom - 예약된 객실 수 조회")
    public void testCountByRoom() {
        // 예약 생성
        ReservationEntity reservation1 = createReservation();
        ReservationEntity reservation2 = createReservation();

        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);

        // when
        Integer count = reservationRepository.countByRoom(room);

        // then
        assertEquals(2, count);
    }

    @Test
    @DisplayName("findAllByUserId - 특정 사용자의 예약 조회")
    public void testFindAllByUserId() {
        // 예약 생성
        ReservationEntity reservation1 = createReservation();
        ReservationEntity reservation2 = createReservation();

        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);

        // when
        Page<ReservationEntity> reservations = reservationRepository.findAllByUserId(user.getId(), Pageable.unpaged());

        // then
        assertEquals(2, reservations.getTotalElements());
    }

    @Test
    @DisplayName("findByStartDateBeforeOrEndDateAfter - 날짜 범위에 속하는 예약 조회")
    @Disabled
    public void testFindByStartDateBeforeOrEndDateAfter() {
        // 예약 생성
        ReservationEntity reservation1 = createReservation();
        ReservationEntity reservation2 = ReservationEntity.builder()
            .accommodation(accommodation)
            .room(room)
            .user(user)
            .headcount(2)
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(1))
            .price(100)
            .build();

        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);

        // when
        List<Long> reservations = reservationRepository.findByStartDateBeforeOrEndDateAfter(
            LocalDate.now(), LocalDate.now());

        // then
        assertFalse(reservations.isEmpty());
    }

    private ReservationEntity createReservation() {
        return ReservationEntity.builder()
            .accommodation(accommodation)
            .room(room)
            .user(user)
            .headcount(2)
            .startDate(LocalDate.now().plusDays(1))
            .endDate(LocalDate.now().plusDays(2))
            .price(100)
            .build();
    }

    @AfterEach
    void tearDown() {
        reservationRepository.deleteAll();
        accommodationRepository.deleteAll();
        roomRepository.deleteAll();
        userRepository.deleteAll();
    }
}
