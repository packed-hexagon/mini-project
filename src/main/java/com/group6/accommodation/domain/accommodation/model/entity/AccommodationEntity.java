package com.group6.accommodation.domain.accommodation.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Table;


@Getter
@Setter
@Entity
@Table(name = "accommodation")
public class AccommodationEntity {
	@Id
	@Column(name = "accommodation_id", nullable = false)
	private Long id;

	@Column(name = "title", nullable = false, length = 32)
	private String title;

	@Column(name = "address", nullable = false, length = 32)
	private String address;

	@Column(name = "address2", length = 32)
	private String address2;

	@Column(name = "areacode")
	private Integer areacode;

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

	@Column(name = "tel", length = 32)
	private String tel;

	@Column(name = "like_count")
	private Integer likeCount;

}