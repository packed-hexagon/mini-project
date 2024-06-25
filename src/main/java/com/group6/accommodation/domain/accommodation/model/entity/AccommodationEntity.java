package com.group6.accommodation.domain.accommodation.model.entity;

import com.group6.accommodation.domain.accommodation.model.enums.Area;
import com.group6.accommodation.domain.accommodation.model.enums.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
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

}