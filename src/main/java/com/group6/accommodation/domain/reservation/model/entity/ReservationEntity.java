package com.group6.accommodation.domain.reservation.model.entity;

import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import com.group6.accommodation.domain.reservation.model.dto.PostReservationRequestDto;
import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import com.group6.accommodation.global.model.entity.TimeStamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.*;
import org.hibernate.annotations.SQLDelete;

@Getter
@Entity
@Table(name = "reservation")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE reservation SET deleted_at = NOW() WHERE reservation_id = ?")
@Builder
public class ReservationEntity extends TimeStamp {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reservation_id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id", referencedColumnName = "user_id")
	private UserEntity user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="room_id", referencedColumnName = "room_id")
	private RoomEntity room;

	@Column(name = "headcount", nullable = false)
	private Integer headcount;

	@Column(name = "price", nullable = false)
	private Integer price;

	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	@Column(name = "end_date", nullable = false)
	private LocalDate endDate;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;


	public static ReservationEntity of(RoomEntity room, UserEntity user, int price, PostReservationRequestDto requestDto) {
		return ReservationEntity.builder()
			.user(user)
			.room(room)
			.price(price)
			.headcount(requestDto.getHeadcount())
			.startDate(requestDto.getStartDate())
			.endDate(requestDto.getEndDate())
			.build();
	}

}
