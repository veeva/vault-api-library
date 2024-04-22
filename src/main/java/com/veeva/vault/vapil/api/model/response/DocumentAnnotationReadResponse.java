/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.common.DocumentAnnotation;

import java.util.List;

/**
 * Model for document annotation read response
 */
public class DocumentAnnotationReadResponse extends VaultResponse {
	@JsonProperty("data")
	public List<DocumentAnnotation> getData() {
		return (List<DocumentAnnotation>) this.get("data");
	}

	public void setData(List<DocumentAnnotation> data) {
		this.set("data", data);
	}

	@JsonProperty("responseDetails")
	public DocumentAnnotation.ResponseDetails getResponseDetails() {
		return (DocumentAnnotation.ResponseDetails) this.get("responseDetails");
	}

	public void setResponseDetails(DocumentAnnotation.ResponseDetails responseDetails) {
		this.set("responseDetails", responseDetails);
	}

	@JsonIgnore
	public boolean isPaginated() {
		if (getResponseDetails() != null) {
			if (getResponseDetails().getPreviousPage() != null || getResponseDetails().getNextPage() != null) {
				return true;
			}

			if (getResponseDetails().getSize() != getResponseDetails().getTotal()) {
				return true;
			}
		}
		return false;
	}
}