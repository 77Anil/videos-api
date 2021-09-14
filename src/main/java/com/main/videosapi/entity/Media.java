package com.main.videosapi.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.transaction.Transactional;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Transactional
@Table(name = "tbl_media")
public class Media {

	public enum Type {
		Video, Audio
	}

	@Id
	@GeneratedValue
	private int id;

	@Size(min = 2, message = "Video name must be of minimum 2 characters")
	private String name;

	@Size(min = 10, message = "Video name must be of minimum 10 characters")
	private String description;

	private String imageurl;

	private String mediaurl;

	@Enumerated(EnumType.STRING)
	private Type mediaType;

	private String bannerurl;

	@Column(columnDefinition = "integer default 0")
	private int status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Category category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private SubCategory subcategory;

	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date datetime;

	public Media() {

	}

	public Media(@Size(min = 2, message = "Video name must be of minimum 2 characters") String name,
			@Size(min = 10, message = "Video name must be of minimum 10 characters") String description,
			String imageurl, String mediaurl, Type mediaType, String bannerurl, int status, Date datetime) {
		super();
		this.name = name;
		this.description = description;
		this.imageurl = imageurl;
		this.mediaurl = mediaurl;
		this.mediaType = mediaType;
		this.bannerurl = bannerurl;
		this.status = status;
		this.datetime = datetime;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	@Override
	public String toString() {
		return "Media [name=" + name + ", description=" + description + ", imageurl=" + imageurl + ", mediaurl="
				+ mediaurl + ", mediaType=" + mediaType + ", bannerurl=" + bannerurl + ", status=" + status + "]";
	}

	public Type getMediaType() {
		return mediaType;
	}

	public void setMediaType(Type mediaType) {
		this.mediaType = mediaType;
	}

	public String getMediaurl() {
		return mediaurl;
	}

	public void setMediaurl(String mediaurl) {
		this.mediaurl = mediaurl;
	}

	public String getBannerurl() {
		return bannerurl;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public void setBannerurl(String bannerurl) {
		this.bannerurl = bannerurl;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public SubCategory getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(SubCategory subcategory) {
		this.subcategory = subcategory;
	}

}
