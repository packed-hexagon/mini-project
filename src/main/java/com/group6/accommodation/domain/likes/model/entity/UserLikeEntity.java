package com.group6.accommodation.domain.likes.model.entity;

import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import com.group6.accommodation.global.model.entity.TimeStamp;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity
@Table(name = "user_like")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserLikeEntity extends TimeStamp {
	@EmbeddedId
	private UserLikeId id;

	@MapsId("userId")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	@MapsId("accommodationId")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "accommodation_id", nullable = false)
	private AccommodationEntity accommodation;

	@Builder
	public UserLikeEntity(UserLikeId id, UserEntity user, AccommodationEntity accommodation) {
		this.id = id;
		this.user = user;
		this.accommodation = accommodation;
	}
}
