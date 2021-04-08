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
 * Model for the Document/Binder relationship response endpoints:<br>
 * POST
 * /api/{version}/objects/documents/{document_id}/versions/{major_version_number_v}/{minor_version_number_v}/relationships
 * <br>
 * DELETE
 * /api/{version}/objects/documents/{document_id}/versions/{major_version_number__v}/{minor_version_number__v}/relationships/{relationship_id}
 */
public class DocumentRelationshipResponse extends VaultResponse {

	@JsonProperty("id")
	public String getId() {
		return this.getString("id");
	}

	public void setId(String id) {
		this.set("id", id);
	}
}
