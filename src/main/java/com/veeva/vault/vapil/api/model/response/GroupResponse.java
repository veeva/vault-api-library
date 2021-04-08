/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

/**
 * Model for the following API calls responses:
 * <p>
 * GET /api/{version}/objects/groups
 * GET /api/{version}/objects/groups/auto
 * GET /api/{version}/objects/groups/{group_id}
 */
public class GroupResponse extends VaultResponse {

	@JsonProperty("id")
	public Long getId() {
		return this.getLong("id");
	}

	public void setId(Long id) {
		this.set("id", id);
	}
}