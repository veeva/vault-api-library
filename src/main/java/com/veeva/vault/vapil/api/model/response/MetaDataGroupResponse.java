/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.metadata.Group;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

import java.util.List;

/**
 * Response model for the following API call:
 * <p>
 * GET /api/{version}/metadata/objects/groups
 */
public class MetaDataGroupResponse extends VaultResponse {

	@JsonProperty("properties")
	public List<Group> getProperties() {
		return (List<Group>) this.get("properties");
	}

	public void setProperties(List<Group> properties) {
		this.set("properties", properties);
	}
}