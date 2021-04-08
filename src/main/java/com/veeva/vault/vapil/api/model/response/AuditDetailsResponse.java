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

/**
 * Model for the following API calls responses:
 * <p>
 * GET /api/{version}/audittrail/{audit_trail_type}
 */
public class AuditDetailsResponse extends VaultResponse {

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

		@JsonProperty("object")
		public DetailsObject getDetailsObject() {
			return (DetailsObject) this.get("object");
		}

		public void setDetailsObject(DetailsObject detailsObject) {
			this.set("object", detailsObject);
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

		public static class DetailsObject extends VaultModel {

			@JsonProperty("label")
			public String getLabel() {
				return this.getString("label");
			}

			public void setLabel(String label) {
				this.set("label", label);
			}

			@JsonProperty("name")
			public String getName() {
				return this.getString("name");
			}

			public void setName(String name) {
				this.set("name", name);
			}

			@JsonProperty("url")
			public String getUrl() {
				return this.getString("url");
			}

			public void setUrl(String url) {
				this.set("url", url);
			}
		}

		/**
		 * Determine if a next page exists for pagination
		 *
		 * @return true if a next page exists
		 */
		public boolean hasNextPage() {
			String nextPage = getNextPage();
			if (nextPage != null && !nextPage.isEmpty())
				return true;
			else
				return false;
		}

		/**
		 * Determine if a previous page exists for pagination
		 *
		 * @return true if a previous page exists
		 */
		public boolean hasPreviousPage() {
			String previousPage = getPreviousPage();
			if (previousPage != null && !previousPage.isEmpty())
				return true;
			else
				return false;
		}
	}
}