package com.group6.accommodation.domain.reservation.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

@Getter
@Setter
@Embeddable
public class ReservationId implements Serializable {
	private static final long serialVersionUID = -4152546925722771716L;
	@Column(name = "reservation_id", nullable = false)
	private Long reservationId;

	@Column(name = "accommodation_id", nullable = false)
	private Long accommodationId;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "room_id", nullable = false)
	private Long roomId;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
			return false;
		ReservationId entity = (ReservationId) o;
		return Objects.equals(this.reservationId, entity.reservationId) &&
			Objects.equals(this.accommodationId, entity.accommodationId) &&
			Objects.equals(this.userId, entity.userId) &&
			Objects.equals(this.roomId, entity.roomId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(reservationId, accommodationId, userId, roomId);
	}

}