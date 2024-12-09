/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

import java.util.List;

public class SdkProfilingSession extends VaultModel {
	@JsonProperty("id")
	public String getId() {
		return getString("id");
	}

	public void setId(String id) {
		this.set("id", id);
	}

	@JsonProperty("label")
	public String getLabel() {
		return getString("label");
	}

	public void setLabel(String label) {
		this.set("label", label);
	}

	@JsonProperty("name")
	public String getName() {
		return getString("name");
	}

	public void setName(String name) {
		this.set("name", name);
	}

	@JsonProperty("description")
	public String getDescription() {
		return getString("description");
	}

	public void setDescription(String description) {
		this.set("description", description);
	}

	@JsonProperty("status")
	public String getStatus() {
		return getString("status");
	}

	public void setStatus(String status) {
		this.set("status", status);
	}

	@JsonProperty("user_id")
	public Long getUserId() {
		return getLong("user_id");
	}

	public void setUserId(Long userId) {
		this.set("user_id", userId);
	}

	@JsonProperty("created_date")
	public String getCreatedDate() {
		return getString("created_date");
	}

	public void setCreatedDate(String createdDate) {
		this.set("created_date", createdDate);
	}

	@JsonProperty("expiration_date")
	public String getExpirationDate() {
		return getString("expiration_date");
	}

	public void setExpirationDate(String expirationDate) {
		this.set("expiration_date", expirationDate);
	}
}
