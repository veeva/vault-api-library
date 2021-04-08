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
import com.veeva.vault.vapil.api.model.VaultModel;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

/**
 * Model for the following API calls responses:
 * <p>
 * GET /api/{version}/objects/documents/{doc_id}/renditions
 * <br>
 * GET /api/{version}/objects/documents/{doc_id}/versions/{major_version}/{minor_version}/renditions
 */
public class DocumentRenditionResponse extends VaultResponse {

	@JsonProperty("renditions")
	public Renditions getRenditions() {
		return (Renditions) this.get("renditions");
	}

	public void setRenditions(Renditions renditions) {
		this.set("renditions", renditions);
	}

	@JsonProperty("renditionTypes")
	public List<String> getRenditionTypes() {
		return (List<String>) this.get("renditionTypes");
	}

	public void setRenditionTypes(List<String> renditionTypes) {
		this.set("renditionTypes", renditionTypes);
	}

	public static class Renditions extends VaultModel {
	}
}
