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
import com.veeva.vault.vapil.api.model.common.DocumentAnnotationReply;

import java.util.List;

/**
 * Model for document annotation reply read response
 */
public class DocumentAnnotationReplyReadResponse extends VaultResponse {
	@JsonProperty("data")
	public List<DocumentAnnotationReply> getData() {
		return (List<DocumentAnnotationReply>) this.get("data");
	}

	public void setData(List<DocumentAnnotationReply> data) {
		this.set("data", data);
	}

	@JsonProperty("responseDetails")
	public DocumentAnnotationReply.ResponseDetails getResponseDetails() {
		return (DocumentAnnotationReply.ResponseDetails) this.get("responseDetails");
	}

	public void setResponseDetails(DocumentAnnotationReply.ResponseDetails responseDetails) {
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