package com.group6.accommodation.domain.room.repository;

import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
	RoomEntity findByAccommodationIdAndRoomId(Long accommodationId, Long roomId);
	List<RoomEntity> findByAccommodation_Id(Long id);
}
