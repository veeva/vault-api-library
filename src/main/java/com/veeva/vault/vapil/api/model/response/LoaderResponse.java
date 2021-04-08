/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.common.LoaderTask;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

import java.util.List;

/**
 * Model for the following API calls responses:
 * <p>
 * GET /api/{version}/services/loader
 */
public class LoaderResponse extends VaultResponse {

	@JsonProperty("job_id")
	public Integer getJobId() {
		return this.getInteger("job_id");
	}

	public void setJobId(Integer jobId) {
		this.set("job_id", jobId);
	}

	@JsonProperty("tasks")
	public List<LoaderTask> getTasks() {
		return (List<LoaderTask>) this.get("tasks");
	}

	public void setTasks(List<LoaderTask> tasks) {
		this.set("tasks", tasks);
	}

	@JsonProperty("url")
	public String getUrl() {
		return this.getString("url");
	}

	public void setUrl(String url) {
		this.set("url", url);
	}
}