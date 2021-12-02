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

/**
 * Model for document deletions API response
 */
public class DocumentDeletionResponse extends VaultResponse {

	@JsonProperty("data")
	public List<DeleteDocument> getData() {
		return (List<DeleteDocument>) this.get("data");
	}

	@JsonProperty("data")
	public void setData(List<DeleteDocument> data) {
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

	@JsonProperty("responseDetails")
	public void setResponseDetails(ResponseDetails responseDetails) {
		this.set("responseDetails", responseDetails);
	}

	public static class DeleteDocument extends VaultModel {

		@JsonProperty("date_deleted")
		public String getDateDeleted() {
			return this.getString("date_deleted");
		}

		@JsonProperty("date_deleted")
		public void setDateDeleted(String dateDeleted) {
			this.set("date_deleted", dateDeleted);
		}

		@JsonProperty("deletion_type")
		public String getDeletionType() {
			return this.getString("deletion_type");
		}

		@JsonProperty("deletion_type")
		public void setDeletionType(String deletionType) {
			this.set("deletion_type", deletionType);
		}

		@JsonProperty("external_id__v")
		public String getExternalId() {
			return this.getString("external_id__v");
		}

		@JsonProperty("external_id__v")
		public void setExternalId(String externalId) {
			this.set("external_id__v", externalId);
		}

		@JsonProperty("global_id__sys")
		public String getGlobalId() {
			return this.getString("global_id__sys");
		}

		@JsonProperty("global_id__sys")
		public void setGlobalId(String globalId) {
			this.set("global_id__sys", globalId);
		}

		@JsonProperty("global_version_id__sys")
		public String getGlobalVersionId() {
			return this.getString("global_version_id__sys");
		}

		@JsonProperty("global_version_id__sys")
		public void setGlobalVersionId(String globalVersionId) {
			this.set("global_version_id__sys", globalVersionId);
		}

		@JsonProperty("id")
		public Integer getId() {
			return this.getInteger("id");
		}

		@JsonProperty("id")
		public void setId(Integer id) {
			this.set("id", id);
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

		@JsonProperty("offset")
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

		@JsonProperty("size")
		public void setSize(Integer size) {
			this.set("size", size);
		}

		@JsonProperty("total")
		public Integer getTotal() {
			return this.getInteger("total");
		}

		@JsonProperty("total")
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