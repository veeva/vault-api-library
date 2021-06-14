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
import com.veeva.vault.vapil.api.model.response.VaultResponse;

import java.util.List;

/**
 * Response model for the following API calls:
 * <p>
 * POST /api/{version}/vobjects/{object_name}/{object_record_id}/actions/cascadedelete
 * <br>
 * POST /api/{version}/objects/documents/batch/actions/fileextract
 */
public class DocumentTokenResponse extends VaultResponse {

	@JsonProperty("tokens")
	public List<Token> getTokens() {
		return (List<Token>) this.get("tokens");
	}

	public void setTokens(List<Token> tokens) {
		this.set("tokens", tokens);
	}

	public static class Token extends VaultModel {

		@JsonProperty("document_id__v")
		public Integer getDocumentId() {
			return this.getInteger("document_id__v");
		}

		public void setDocumentId(Integer documentId) {
			this.set("document_id__v", documentId);
		}

		@JsonProperty("token__v")
		public String getToken() {
			return this.getString("token__v");
		}

		public void setToken(String token) {
			this.set("token__v", token);
		}

		@JsonProperty("errorType")
		public String getErrorType() {
			return this.getString("errorType");
		}

		public void setErrorType(String errorType) {
			this.set("errorType", errorType);
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