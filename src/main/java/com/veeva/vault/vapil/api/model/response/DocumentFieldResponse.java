/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.metadata.DocumentField;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

import java.util.List;

/**
 * Model for the document field response
 */
public class DocumentFieldResponse extends VaultResponse {

	@JsonProperty("properties")
	public List<DocumentField> getProperties() {
		return (List<DocumentField>) this.get("properties");
	}

	public void setProperties(List<DocumentField> properties) {
		this.set("properties", properties);
	}
}