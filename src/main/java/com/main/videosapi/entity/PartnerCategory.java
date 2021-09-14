package com.main.videosapi.entity;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.transaction.Transactional;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Embeddable
@Table(name = "partner_categories")
@Transactional
public class PartnerCategory {

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Partner partner;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Category category;

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

}
