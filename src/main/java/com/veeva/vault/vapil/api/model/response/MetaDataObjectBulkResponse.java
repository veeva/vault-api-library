/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.metadata.Object;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

import java.util.List;

/**
 * Response model for the following API calls:
 * <p>
 * GET /api/{version}/metadata/vobjects
 */
public class MetaDataObjectBulkResponse extends VaultResponse {

	@JsonProperty("objects")
	public List<Object> getObjects() {
		return (List<Object>) this.get("objects");
	}

	public void setObjects(List<Object> objects) {
		this.set("objects", objects);
	}
}