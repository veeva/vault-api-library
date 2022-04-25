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
 * Response model for an EDL Matched Document Add and Remove Response
 */
public class EDLMatchedDocumentResponse extends VaultResponse {

	@JsonProperty("data")
	public List<EdlMatchedDocument> getData() {
		return (List<EdlMatchedDocument>) this.get("data");
	}

	public void setData(List<EdlMatchedDocument> data) {
		this.set("data", data);
	}

	public static class EdlMatchedDocument extends VaultModel {

		@JsonProperty("id")
		public String getId() {
			return this.getString("id");
		}

		@JsonProperty("id")
		public void setId(String id) {
			this.set("id", id);
		}

		@JsonProperty("document_id")
		public Integer getDocumentId() {
			return this.getInteger("document_id");
		}

		@JsonProperty("document_id")
		public void setDocumentId(Integer documentId) {
			this.set("document_id", documentId);
		}

		@JsonProperty("lock")
		public Boolean getLock() {
			return this.getBoolean("lock");
		}

		@JsonProperty("lock")
		public void setLock(Boolean lock) {
			this.set("lock", lock);
		}

		@JsonProperty("major_version_number__v")
		public Integer getMajorVersionNumber() {
			return this.getInteger("major_version_number__v");
		}

		@JsonProperty("major_version_number__v")
		public void setMajorVersionNumber(Integer majorVersionNumber) {
			this.set("major_version_number__v", majorVersionNumber);
		}

		@JsonProperty("minor_version_number__v")
		public Integer getMinorVersionNumber() {
			return this.getInteger("minor_version_number__v");
		}

		@JsonProperty("minor_version_number__v")
		public void setMinorVersionNumber(Integer minorVersionNumber) {
			this.set("minor_version_number__v", minorVersionNumber);
		}

		@JsonProperty("responseStatus")
		public String getResponseStatus() {
			return this.getString("responseStatus");
		}

		public void setResponseStatus(String responseStatus) {
			this.set("responseStatus", responseStatus);
		}

		@JsonProperty("errors")
		public List<APIResponseError> getErrors() {
			return (List<APIResponseError>) this.get("errors");
		}

		public void setErrors(List<APIResponseError> errors) {
			this.set("errors", errors);
		}
	}
}