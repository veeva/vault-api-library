/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

import java.util.List;

/**
 * Model for the document annotation import response
 */
public class DocumentAnnotationImportResponse extends VaultResponse {

	@JsonProperty("failures")
	public Integer getFailures() {
		return this.getInteger("failures");
	}

	@JsonProperty("failures")
	public void setFailures(Integer failures) {
		this.set("failures", failures);
	}

	@JsonProperty("new")
	public Integer getNewCount() {
		return this.getInteger("new");
	}

	@JsonProperty("new")
	public void setNewCount(Integer newCount) {
		this.set("new", newCount);
	}

	@JsonProperty("replies")
	public Integer getReplies() {
		return this.getInteger("replies");
	}

	@JsonProperty("replies")
	public void setReplies(Integer replies) {
		this.set("replies", replies);
	}

}