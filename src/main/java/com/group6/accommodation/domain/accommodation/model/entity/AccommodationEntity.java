package com.group6.accommodation.domain.accommodation.model.entity;

import com.group6.accommodation.domain.likes.model.entity.UserLikeEntity;
import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import com.group6.accommodation.global.model.entity.TimeStamp;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "accommodation")
public class AccommodationEntity extends TimeStamp {
	@Id
	@Column(name = "accommodation_id", nullable = false)
	private Long id;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "address", nullable = false)
	private String address;

	@Column(name = "areacode", nullable = false, length = 32)
	private String areacode;

	@Column(name = "category", nullable = false, length = 32)
	private String category;

	@Column(name = "image", nullable = false, length = 300)
	private String image;

	@Column(name = "thumbnail", nullable = false, length = 300)
	private String thumbnail;

	@Column(name = "latitude", nullable = false)
	private Double latitude;

	@Column(name = "longitude", nullable = false)
	private Double longitude;

	@Column(name = "tel", nullable = false, length = 64)
	private String tel;

	@Column(name = "like_count", nullable = false)
	private Integer likeCount;

	@Column(name = "review_count", nullable = false)
	private Integer reviewCount;

	@Column(name = "total_rating", nullable = false)
	private Double totalRating;

	@OneToMany(mappedBy = "accommodation", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RoomEntity> rooms = new ArrayList<>();

	@OneToMany(mappedBy = "accommodation", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserLikeEntity> userLikes = new ArrayList<>();

//	@OneToMany(mappedBy = "accommodation", cascade = CascadeType.ALL, orphanRemoval = true)
//	private List<ReviewEntity> reviews = new ArrayList<>();

	@Builder
	public AccommodationEntity(
			Long id, String title, String address, String areacode, String category, String image, String thumbnail, Double latitude, Double longitude,
			String tel, int likeCount, int reviewCount, double totalRating
	) {
		this.id = id;
		this.title = title;
		this.address = address;
		this.areacode = areacode;
		this.category = category;
		this.image = image;
		this.thumbnail = thumbnail;
		this.latitude = latitude;
		this.longitude = longitude;
		this.tel = tel;
		this.likeCount = likeCount;
		this.reviewCount = reviewCount;
		this.totalRating = totalRating;
	}
}