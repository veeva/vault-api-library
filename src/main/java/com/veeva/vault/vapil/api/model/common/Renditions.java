/**
 * --------------------------------------------------------------------
 * Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 * This code is based on pre-existing content developed and
 * owned by Veeva Systems Inc. and may only be used in connection
 * with the deliverable with which it was provided to Customer.
 * --------------------------------------------------------------------
 */

package com.veeva.vault.vapil.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

import java.util.List;

/**
 * Model for User data
 */
public class Renditions extends VaultModel {

	@JsonProperty("viewable_rendition__v")
	public String getViewableRendition() {
		return this.getString("viewable_rendition__v");
	}

	public void setViewableRendition(String viewableRendition) {
		this.set("viewable_rendition__v", viewableRendition);
	}

}
