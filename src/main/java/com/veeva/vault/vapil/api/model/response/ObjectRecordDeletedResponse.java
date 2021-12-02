/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

/**
 * Model for Deleted Object Records recycle bin endpoint:
 * <p>
 * GET /api/{version}/objects/deletions/vobjects/{object_name}
 */
public class ObjectRecordDeletedResponse extends VaultResponse {

	@JsonProperty("data")
	public List<DeletedRecord> getData() {
		return (List<DeletedRecord>) this.get("data");
	}

	public void setData(List<DeletedRecord> data) {
		this.set("data", data);
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

	@JsonProperty("responseDetails")
	public ResponseDetails getResponseDetails() {
		return (ResponseDetails) this.get("responseDetails");
	}

	public void setResponseDetails(ResponseDetails responseDetails) {
		this.set("responseDetails", responseDetails);
	}

	public static class DeletedRecord extends VaultModel {

		@JsonProperty("date_deleted")
		public String getDateDeleted() {
			return this.getString("date_deleted");
		}

		public void setDateDeleted(String dateDeleted) {
			this.set("date_deleted", dateDeleted);
		}

		@JsonProperty("id")
		public String getId() {
			return this.getString("id");
		}

		public void setId(String id) {
			this.set("id", id);
		}
	}

	public class ResponseDetails extends VaultModel {

		@JsonProperty("limit")
		public Integer getLimit() {
			return this.getInteger("limit");
		}

		public void setLimit(Integer limit) {
			this.set("limit", limit);
		}

		@JsonProperty("next_page")
		public String getNextPage() {
			return this.getString("next_page");
		}

		public void setNextPage(String nextPage) {
			this.set("next_page", nextPage);
		}

		@JsonProperty("offset")
		public Integer getOffset() {
			return this.getInteger("offset");
		}

		public void setOffset(Integer offset) {
			this.set("offset", offset);
		}

		@JsonProperty("previous_page")
		public String getPreviousPage() {
			return this.getString("previous_page");
		}

		public void setPreviousPage(String previousPage) {
			this.set("previous_page", previousPage);
		}

		@JsonProperty("size")
		public Integer getSize() {
			return this.getInteger("size");
		}

		public void setSize(Integer size) {
			this.set("size", size);
		}

		@JsonProperty("total")
		public Integer getTotal() {
			return this.getInteger("total");
		}

		public void setTotal(Integer total) {
			this.set("total", total);
		}

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