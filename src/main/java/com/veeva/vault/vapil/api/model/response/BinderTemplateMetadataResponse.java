/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.metadata.Template;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

import java.util.List;

/**
 * Model for the following API calls responses:
 * <p>
 * GET /api/{version}/metadata/objects/binders/templates
 */
public class BinderTemplateMetadataResponse extends VaultResponse {

	@JsonProperty("data")
	public List<Template> getData() {
		return (List<Template>) get("data");
	}

	public void setData(List<Template> data) {
		this.set("data", data);
	}
}