package com.main.videosapi.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.transaction.Transactional;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Transactional
@Table(name = "tbl_cat")
public class Category {

	@Id
	@GeneratedValue
	private int id;

	@Size(min = 2, message = "Category name must be of two character")
	private String name;

	@Column(columnDefinition = "integer default 0")
	private int status;

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "category")
	@JsonIgnore
	private List<SubCategory> subcategory;

	@Override
	public String toString() {
		return "Category [name=" + name + ", status=" + status + "]";
	}

	public Category() {

	}

	public Category(@Size(min = 2, message = "Category name must be of two character") String name, int status) {
		super();
		this.name = name;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<SubCategory> getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(List<SubCategory> subcategory) {
		this.subcategory = subcategory;
	}

}
