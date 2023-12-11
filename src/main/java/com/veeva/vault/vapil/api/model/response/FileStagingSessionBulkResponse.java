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
import com.veeva.vault.vapil.api.model.VaultModel;
import com.veeva.vault.vapil.api.model.common.ResumableUploadSession;

import java.util.List;

public class FileStagingSessionBulkResponse extends VaultResponse {

	@JsonProperty("data")
	public List<ResumableUploadSession> getData() {
		return (List<ResumableUploadSession>) this.get("data");
	}

	public void setData(List<ResumableUploadSession> data) {
		this.set("data", data);
	}

	@JsonIgnore
	public boolean isPaginated() {
		if (getResponseDetails() != null && getResponseDetails().getNextPage() != null) {
			return true;
		}
		return false;
	}

	@JsonProperty("responseDetails")
	public FileStagingItemBulkResponse.ResponseDetails getResponseDetails() {
		return (FileStagingItemBulkResponse.ResponseDetails) this.get("responseDetails");
	}

	public void setResponseDetails(FileStagingItemBulkResponse.ResponseDetails responseDetails) {
		this.set("responseDetails", responseDetails);
	}

	public static class ResponseDetails extends VaultModel {

		@JsonProperty("next_page")
		public String getNextPage() {
			return this.getString("next_page");
		}

		public void setNextPage(String nextPage) {
			this.set("next_page", nextPage);
		}
	}
}