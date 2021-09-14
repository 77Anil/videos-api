package com.main.videosapi.entity;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Embeddable
@Table(name = "partner_roles")
public class PartnerRoles {

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Partner partner;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Role role;

	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
