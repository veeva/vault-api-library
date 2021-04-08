/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

/**
 * Model for the metadata component records
 */
public class ComponentRecord extends VaultModel {
	@JsonProperty("active")
	public Boolean getActive() {
		return getBoolean("active");
	}

	public void setActive(Boolean active) {
		this.set("active", active);
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
}
