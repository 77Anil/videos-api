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
import com.sun.istack.NotNull;

@Entity
@Transactional
@Table(name = "tbl_teaser")
public class Teaser {

	public enum Type {
		Video, Audio
	}

	@Id
	@GeneratedValue
	private int id;

	@Size(min = 2, message = "Name should be of Two Characters")
	private String name;

	@Size(min = 2, message = "Description should be of minimum 10 characters")
	private String description;

	@NotNull
	private String mediaurl;

	@NotNull
	private String imgurl;

	private String bannerurl;

	@Column(columnDefinition = "integer default 0")
	private int status;

	@Enumerated(EnumType.STRING)
	private Type type;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Category category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private SubCategory subCategory;

	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date datetime;

	public Teaser() {
	}

	public Teaser(@Size(min = 2, message = "Name should be of Two Characters") String name,
			@Size(min = 2, message = "Description should be of minimum 10 characters") String description,
			String mediaurl, String imgurl, String bannerurl, int status, Type type, Date datetime) {
		super();
		this.name = name;
		this.description = description;
		this.mediaurl = mediaurl;
		this.imgurl = imgurl;
		this.bannerurl = bannerurl;
		this.status = status;
		this.type = type;
		this.datetime = datetime;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
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

	public String getMediaurl() {
		return mediaurl;
	}

	public void setMediaurl(String mediaurl) {
		this.mediaurl = mediaurl;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getBannerurl() {
		return bannerurl;
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

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public SubCategory getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(SubCategory subCategory) {
		this.subCategory = subCategory;
	}

}
