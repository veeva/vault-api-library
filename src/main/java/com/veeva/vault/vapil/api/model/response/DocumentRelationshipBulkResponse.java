/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Model for the Document Relationship Bulk responses
 */
public class DocumentRelationshipBulkResponse extends VaultResponse {

	@JsonProperty("data")
	public List<DocumentRelationshipResponse> getData() {
		return (List<DocumentRelationshipResponse>) this.get("data");
	}

	public void setData(List<DocumentRelationshipResponse> data) {
		this.set("data", data);
	}
}