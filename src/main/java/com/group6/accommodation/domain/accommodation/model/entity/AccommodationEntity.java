package com.group6.accommodation.domain.accommodation.model.entity;

import com.group6.accommodation.domain.room.model.entity.RoomEntity;
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
public class AccommodationEntity {
	@Id
	@Column(name = "accommodation_id", nullable = false)
	private Long id;

	@Column(name = "title", nullable = false, length = 255)
	private String title;

	@Column(name = "address", nullable = false, length = 255)
	private String address;

	@Column(name = "address2", length = 32)
	private String address2;

	@Column(name = "areacode", length = 32)
	private String areacode;

	@Column(name = "sigungucode")
	private Integer sigungucode;

	@Column(name = "category", length = 32)
	private String category;

	@Column(name = "image", length = 300)
	private String image;

	@Column(name = "thumbnail", length = 300)
	private String thumbnail;

	@Column(name = "latitude", nullable = false)
	private Double latitude;

	@Column(name = "longitude", nullable = false)
	private Double longitude;

	@Column(name = "mlevel")
	private Integer mlevel;

	@Column(name = "tel", length = 64)
	private String tel;

	@Column(name = "like_count")
	private Integer likeCount;

	@Column(name = "rating")
	private Double rating;

	@OneToMany(mappedBy = "accommodation", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RoomEntity> rooms = new ArrayList<>();

	@Builder
	public AccommodationEntity(Long id, String title, String address, String address2, String areacode, Integer sigungucode,
							   String category, String image, String thumbnail, Double latitude, Double longitude,
							   Integer mlevel, String tel, Integer likeCount, Double rating) {
		this.id = id;
		this.title = title;
		this.address = address;
		this.address2 = address2;
		this.areacode = areacode;
		this.sigungucode = sigungucode;
		this.category = category;
		this.image = image;
		this.thumbnail = thumbnail;
		this.latitude = latitude;
		this.longitude = longitude;
		this.mlevel = mlevel;
		this.tel = tel;
		this.likeCount = likeCount;
		this.rating = rating;
	}

}