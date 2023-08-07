/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.VaultModel;

import java.util.List;

public class Job extends VaultModel {

	@JsonProperty("created_by")
	public Integer getCreatedBy() {
		return getInteger("created_by");
	}

	public void setCreatedBy(Integer createdBy) {
		this.set("created_by", createdBy);
	}

	@JsonProperty("created_date")
	public String getCreatedDate() {
		return getString("created_date");
	}

	public void setCreatedDate(String createdDate) {
		this.set("created_date", createdDate);
	}

	@JsonProperty("job_id")
	public Integer getJobId() {
		return getInteger("job_id");
	}

	public void setJobId(Integer jobId) {
		this.set("job_id", jobId);
	}

	@JsonProperty("links")
	public List<Link> getLinks() {
		return (List<Link>) get("links");
	}

	public void setLinks(List<Link> links) {
		this.set("links", links);
	}


	@JsonProperty("method")
	public String getMethod() {
		return getString("method");
	}

	public void setMethod(String method) {
		this.set("method", method);
	}

	@JsonProperty("run_end_date")
	public String getRunEndDate() {
		return getString("run_end_date");
	}

	public void setRunEndDate(String runEndDate) {
		this.set("run_end_date", runEndDate);
	}

	@JsonProperty("run_start_date")
	public String getRunStartDate() {
		return getString("run_start_date");
	}

	public void setRunStartDate(String runStartDate) {
		this.set("run_start_date", runStartDate);
	}

	@JsonProperty("status")
	public String getStatus() {
		return getString("status");
	}

	public void setStatus(String status) {
		this.set("status", status);
	}

	@JsonProperty("title")
	public String getTitle() {
		return getString("title");
	}

	public void setTitle(String title) {
		this.set("title", title);
	}

	public static class Link extends VaultModel {
		@JsonProperty("rel")
		public String getRel() {
			return getString("rel");
		}

		public void setRel(String rel) {
			this.set("rel", rel);
		}

		@JsonProperty("href")
		public String getHref() {
			return getString("href");
		}

		public void setHref(String href) {
			this.set("href", href);
		}

		@JsonProperty("method")
		public String getMethod() {
			return getString("method");
		}

		public void setMethod(String method) {
			this.set("method", method);
		}

		@JsonProperty("accept")
		public String getAccept() {
			return getString("accept");
		}

		public void setAccept(String accept) {
			this.set("accept", accept);
		}
	}
}
