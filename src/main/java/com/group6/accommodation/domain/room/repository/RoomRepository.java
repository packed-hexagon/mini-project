package com.group6.accommodation.domain.room.repository;

import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import com.group6.accommodation.global.exception.error.RoomErrorCode;
import com.group6.accommodation.global.exception.type.RoomException;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Lock;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
	List<RoomEntity> findByAccommodation_Id(Long id);

	Optional<RoomEntity> findByAccommodation_IdAndRoomId(Long id, Long roomId);

	List<RoomEntity> findByRoomIdIn(List<Long> ids);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<RoomEntity> findById(Long id);


    default RoomEntity getById(Long id) {
        return findById(id).orElseThrow(
            () -> new RoomException(RoomErrorCode.NOT_FOUND_ROOM)
        );
    }

    @Query("SELECT MIN(r.weekdaysFee) FROM RoomEntity r WHERE r.accommodation.id = :accommodationId AND r.weekdaysFee > 0")
    Integer findMinWeekDaysFeeByAccommodation_Id(Long accommodationId);

}