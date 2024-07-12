package com.group6.accommodation.domain.room.repository;

import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import com.group6.accommodation.global.exception.error.RoomErrorCode;
import com.group6.accommodation.global.exception.type.RoomException;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
	List<RoomEntity> findByAccommodation_Id(Long id);

	Optional<RoomEntity> findByAccommodation_IdAndRoomId(Long id, Long roomId);

	List<RoomEntity> findByRoomIdIn(List<Long> ids);

	@Query("select r from RoomEntity r where r.checkIn >= ?1 and r.checkOut <= ?2")
	List<RoomEntity> findAvailableRooms(
		@NotNull LocalDate checkIn, @NotNull LocalDate checkOut);

    @NonNull
    default RoomEntity getById(@NonNull Long id) {
        return findById(id).orElseThrow(
                () -> new RoomException(RoomErrorCode.NOT_FOUND_ROOM)
        );
    }
}
