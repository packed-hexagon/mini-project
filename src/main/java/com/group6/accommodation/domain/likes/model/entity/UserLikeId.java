package com.group6.accommodation.domain.likes.model.entity;

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
public class UserLikeId implements Serializable {
	private static final long serialVersionUID = -3434089162790855360L;
	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "accommodation_id", nullable = false)
	private Long accommodationId;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
			return false;
		UserLikeId entity = (UserLikeId) o;
		return Objects.equals(this.accommodationId, entity.accommodationId) &&
			Objects.equals(this.userId, entity.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(accommodationId, userId);
	}

}