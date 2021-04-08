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
 * Response model for the following API calls:
 * <p>
 * GET /api/{version}/code/{class_name}
 * <br>
 * PUT /api/{version}/code/{class_name}/{enable || disable}
 * <br>
 * PUT /api/{version}/code
 * <br>
 * DELETE /api/{version}/code
 */
public class SDKResponse extends VaultResponse {

	@JsonProperty("url")
	public String getUrl() {
		return this.getString("url");
	}

	public void setUrl(String url) {
		this.set("url", url);
	}
}