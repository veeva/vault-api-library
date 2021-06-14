/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.common.Job;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

/**
 * Response model for the API Job Status response
 */
public class JobStatusResponse extends VaultResponse {

	@JsonProperty("data")
	public Job getData() {
		return (Job) this.get("data");
	}

	public void setData(Job data) {
		this.set("data", data);
	}
}