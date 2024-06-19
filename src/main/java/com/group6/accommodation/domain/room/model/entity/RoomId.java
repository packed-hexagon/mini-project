package com.group6.accommodation.domain.room.model.entity;

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
public class RoomId implements Serializable {
	private static final long serialVersionUID = 1109769349436964333L;
	@Column(name = "room_id", nullable = false)
	private Long roomId;

	@Column(name = "accommodation_id", nullable = false)
	private Long accommodationId;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
			return false;
		RoomId entity = (RoomId) o;
		return Objects.equals(this.accommodationId, entity.accommodationId) &&
			Objects.equals(this.roomId, entity.roomId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(accommodationId, roomId);
	}

}