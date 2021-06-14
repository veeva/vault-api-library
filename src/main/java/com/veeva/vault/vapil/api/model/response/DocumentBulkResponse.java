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
 * Model for document create API response
 */
public class DocumentBulkResponse extends VaultResponse {

	@JsonProperty("data")
	public List<DocumentResponse> getData() {
		return (List<DocumentResponse>) get("data");
	}

	@JsonProperty("data")
	public void setData(List<DocumentResponse> data) {
		this.set("data", data);
	}

	@Override
	public boolean hasErrors() {
		if (super.hasErrors()) return true;

		List<DocumentResponse> documentResponses = getData();
		if (documentResponses == null || documentResponses.size() == 0)
			return true;
		else {
			for (DocumentResponse documentResponse : documentResponses) {
				if (!documentResponse.isSuccessful())
					return true;
			}
		}

		return false;
	}
}