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
import com.veeva.vault.vapil.api.model.common.ObjectRecord;
import com.veeva.vault.vapil.api.model.metadata.VaultObject;

import java.util.List;

/**
 * Model for the following API calls responses for the Object Record CRUD
 * operations:
 * <p>
 * GET /api/{version}/vobjects/{object_name} <br>
 */
public class ObjectRecordCollectionResponse extends VaultResponse {

	@JsonProperty("data")
	public List<ObjectRecord> getData() {
		return (List<ObjectRecord> ) this.get("data");
	}

	public void setData(List<ObjectRecord> data) {
		this.set("data", data);
	}

	@JsonIgnore
	public boolean isPaginated() {
		if (getResponseDetails() != null) {
			if (getResponseDetails().getPreviousPage() != null || getResponseDetails().getNextPage() != null) {
				return true;
			}
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

	public static class ResponseDetails extends VaultModel {

		@JsonProperty("limit")
		public Integer getLimit() {
			return this.getInteger("limit");
		}

		@JsonProperty("limit")
		public void setLimit(Integer limit) {
			this.set("limit", limit);
		}

		@JsonProperty("object")
		public VaultObject getObject() {
			return (VaultObject) this.get("object");
		}

		public void setObject(VaultObject object) {
			this.set("object", object);
		}

		@JsonProperty("offset")
		public Integer getOffset() {
			return this.getInteger("offset");
		}

		@JsonProperty("offset")
		public void setOffset(Integer offset) {
			this.set("offset", offset);
		}

		@JsonProperty("total")
		public Integer getTotal() {
			return this.getInteger("total");
		}

		@JsonProperty("total")
		public void setTotal(Integer total) {
			this.set("total", total);
		}

		@JsonProperty("url")
		public String getUrl() {
			return this.getString("url");
		}

		public void setUrl(String url) {
			this.set("url", url);
		}

		@JsonProperty("next_page")
		public String getNextPage() { return this.getString("next_page"); }

		public void setNextPage(String nextPage) { this.set("next_page", nextPage);}

		@JsonProperty("previous_page")
		public String getPreviousPage() { return this.getString("previous_page"); }

		public void setPreviousPage(String previousPage) { this.set("previous_page", previousPage); }

		/**
		 * Determine if a next page exists for pagination
		 *
		 * @return true if a next page exists
		 */
		@JsonIgnore
		public boolean hasNextPage() {
			return getNextPage() != null && !getNextPage().isEmpty();
		}

		/**
		 * Determine if a previous page exists for pagination
		 *
		 * @return true if a previous page exists
		 */
		@JsonIgnore
		public boolean hasPreviousPage() {
			return getPreviousPage() != null && !getPreviousPage().isEmpty();
		}
	}
}