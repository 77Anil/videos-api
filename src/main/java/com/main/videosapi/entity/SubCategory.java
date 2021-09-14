package com.main.videosapi.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.transaction.Transactional;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tbl_sub_cat")
@Transactional
public class SubCategory {

	@Id
	@GeneratedValue
	private int id;

	@Size(min = 2, message = "SubCategory Name must be of two character")
	private String name;

	@Column(columnDefinition = "integer default 0")
	private String status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Category category;

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "subcategory")
	@JsonIgnore
	private List<Media> media;

	public SubCategory() {

	}

	public SubCategory(@Size(min = 2, message = "SubCategory Name must be of two character") String name,
			String status) {
		super();
		this.name = name;
		this.status = status;
	}

	public List<Media> getMedia() {
		return media;
	}

	public void setMedia(List<Media> media) {
		this.media = media;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

}
