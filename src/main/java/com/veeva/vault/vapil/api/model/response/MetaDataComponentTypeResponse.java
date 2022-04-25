/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.common.ComponentType;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

import java.util.List;

/**
 * Response model for the following API calls:
 * <p>
 * /api/{version}/metadata/components/{component_type}
 */
public class MetaDataComponentTypeResponse extends VaultResponse {

	@JsonProperty("data")
	public ComponentType getData() {
		return (ComponentType) this.get("data");
	}

	public void setData(ComponentType data) {
		this.set("data", data);
	}
}