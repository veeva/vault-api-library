/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.common.ComponentType;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

/**
 * Response model for the following API calls:
 * <p>
 * /api/{version}/metadata/components
 */
public class MetaDataComponentTypeBulkResponse extends VaultResponse {

	@JsonProperty("data")
	public List<ComponentType> getData() {
		return (List<ComponentType>) this.get("data");
	}

	public void setData(List<ComponentType> data) {
		this.set("data", data);
	}
}