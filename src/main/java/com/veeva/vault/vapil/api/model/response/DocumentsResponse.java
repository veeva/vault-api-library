/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.common.Document;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

import java.util.List;

/**
 * Model for all documents API Response
 */
public class DocumentsResponse extends VaultResponse {

	@JsonProperty("documents")
	public List<Document> getDocuments() {
		return (List<Document>) this.get("documents");
	}

	public void setDocuments(List<Document> documents) {
		this.set("documents", documents);
	}

	@JsonProperty("limit")
	public Integer getLimit() {
		return this.getInteger("limit");
	}

	public void setLimit(Integer limit) {
		this.set("limit", limit);
	}

	@JsonProperty("size")
	public Integer getSize() {
		return this.getInteger("size");
	}

	public void setSize(Integer size) {
		this.set("size", size);
	}

	@JsonProperty("start")
	public Integer getStart() {
		return this.getInteger("start");
	}

	public void setStart(Integer start) {
		this.set("start", start);
	}
}