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
import com.veeva.vault.vapil.api.model.response.VaultResponse;

import java.util.List;

public class FileStagingItemBulkResponse extends VaultResponse {

	@JsonProperty("data")
	public List<FileStagingItem> getData() {
		return (List<FileStagingItem>) this.get("data");
	}

	public void setData(List<FileStagingItem> data) {
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
	public ResponseDetails getResponseDetails() {
		return (ResponseDetails) this.get("responseDetails");
	}

	public void setResponseDetails(ResponseDetails responseDetails) {
		this.set("responseDetails", responseDetails);
	}

	public static class FileStagingItem extends VaultModel {

		@JsonProperty("kind")
		public String getKind() {
			return getString("kind");
		}

		public void setKind(String kind) {
			this.set("kind", kind);
		}

		@JsonProperty("path")
		public String getPath() {
			return getString("path");
		}

		public void setPath(String path) {
			this.set("path", path);
		}

		@JsonProperty("name")
		public String getName() {
			return getString("name");
		}

		public void setName(String name) {
			this.set("name", name);
		}

		@JsonProperty("size")
		public Long getSize() {
			return getLong("size");
		}

		public void setSize(Long size) {
			this.set("size", size);
		}

		@JsonProperty("modified_date")
		public String getModifiedDate() {
			return getString("modified_date");
		}

		public void setModifiedDate(String modifiedDate) {
			this.set("modified_date", modifiedDate);
		}
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