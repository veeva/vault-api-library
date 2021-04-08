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
 * Response model for the API Job Task response
 */
public class JobTaskResponse extends VaultResponse {

	@JsonProperty("responseDetails")
	public ResponseDetails getResponseDetails() {
		return (ResponseDetails) this.get("responseDetails");
	}

	@JsonProperty("responseDetails")
	public void setResponseDetails(ResponseDetails responseDetails) {
		this.set("responseDetails", responseDetails);
	}

	@JsonProperty("tasks")
	public List<JobTask> getTasks() {
		return (List<JobTask>) this.get("tasks");
	}

	public void setTasks(List<JobTask> tasks) {
		this.set("tasks", tasks);
	}

	@JsonProperty("url")
	public String getUrl() {
		return this.getString("url");
	}

	public void setUrl(String url) {
		this.set("url", url);
	}

	public static class JobTask extends VaultModel {
		@JsonProperty("id")
		public String getId() {
			return getString("id");
		}

		public void setId(String id) {
			this.set("id", id);
		}

		@JsonProperty("status")
		public String getStatus() {
			return getString("status");
		}

		public void setStatus(String status) {
			this.set("status", status);
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

		@JsonProperty("total")
		public Integer getTotal() {
			return this.getInteger("total");
		}

		@JsonProperty("total")
		public void setTotal(Integer total) {
			this.set("total", total);
		}
	}
}