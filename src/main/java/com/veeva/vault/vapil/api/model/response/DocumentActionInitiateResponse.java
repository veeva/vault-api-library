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
 * PUT /api/{version}/objects/{documents_or_binders}/{id}/versions/{major_version}/{minor_version}/lifecycle_actions/{name__v}
 */
public class DocumentActionInitiateResponse extends VaultResponse {

	@JsonProperty("id")
	public Integer getId() {
		return this.getInteger("id");
	}

	@JsonProperty("id")
	public void setId(Integer id) {
		this.set("id", id);
	}

	@JsonProperty("workflow_id__v")
	public Integer getWorkFlowId() {
		return this.getInteger("workflow_id__v");
	}

	@JsonProperty("workflow_id__v")
	public void setWorkFlowId(Integer workflowId) {
		this.set("workflow_id__v", workflowId);
	}
}