/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.*;
import com.veeva.vault.vapil.api.model.common.DocumentTemplate;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

/**
 * Model for the document template response
 */
public class DocumentTemplateResponse extends VaultResponse {

	@JsonProperty("data")
	public DocumentTemplate getData() {
		return (DocumentTemplate) this.get("data");
	}

	public void setData(DocumentTemplate data) {
		this.set("data", data);
	}

	@JsonProperty("name__v")
	public String getName() {
		return this.getString("name__v");
	}

	public void setName(String name) {
		this.set("name__v", name);
	}
}