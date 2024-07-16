package com.group6.accommodation.domain.room.repository;

import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import com.group6.accommodation.global.exception.error.RoomErrorCode;
import com.group6.accommodation.global.exception.type.RoomException;
import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
	List<RoomEntity> findByAccommodation_Id(Long id);

	Optional<RoomEntity> findByAccommodation_IdAndRoomId(Long id, Long roomId);

	@NonNull
	default RoomEntity getByAccommodation_IdAndRoomId(@NonNull Long id, @NotNull Long roomId) {
		return this.findByAccommodation_IdAndRoomId(id, roomId).orElseThrow(
			() -> new RoomException(RoomErrorCode.NOT_FOUND_ROOM)
		);
	}

	default List<RoomEntity> getByAccommodation_Id(Long id) {
		List<RoomEntity> roomEntityList = this.findByAccommodation_Id(id);
		if (roomEntityList.isEmpty()) {
			throw new RoomException(RoomErrorCode.NOT_FOUND_ACCOMMODATION);
		}
		return roomEntityList;
	}

}