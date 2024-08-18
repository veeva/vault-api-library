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
 * Model for Document Annotation Read Data
 */
public class DocumentAnnotationResponse extends VaultModel {
	@JsonProperty("responseStatus")
	public String getResponseStatus() {
		return this.getString("responseStatus");
	}

	@JsonProperty("responseStatus")
	public void setResponseStatus(String responseStatus) {
		this.set("responseStatus", responseStatus);
	}

	@JsonProperty("document_version_id__sys")
	public String getDocumentVersionId() {
		return this.getString("document_version_id__sys");
	}

	@JsonProperty("document_version_id__sys")
	public void setDocumentVersionId(String documentVersionId) {
		this.set("document_version_id__sys", documentVersionId);
	}

	@JsonProperty("global_version_id__sys")
	public String getGlobalVersionId() {
		return this.getString("global_version_id__sys");
	}

	@JsonProperty("global_version_id__sys")
	public void setGlobalVersionId(String globalVersionId) {
		this.set("global_version_id__sys", globalVersionId);
	}

	@JsonProperty("id__sys")
	public String getId() {
		return this.getString("id__sys");
	}

	@JsonProperty("id__sys")
	public void setId(String id) {
		this.set("id__sys", id);
	}

	@JsonProperty("errors")
	public List<Error> getErrors() {
		return (List<Error>) this.get("errors");
	}

	public void setErrors(List<Error> errors) {
		this.set("errors", errors);
	}

	public static class Error extends VaultModel {

		@JsonProperty("type")
		public String getType() {
			return this.getString("type");
		}

		public void setType(String type) {
			this.set("type", type);
		}

		@JsonProperty("message")
		public String getMessage() {
			return this.getString("message");
		}

		public void setMessage(String message) {
			this.set("message", message);
		}
	}
}
