/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonAlias;
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
public class JobCreateResponse extends VaultResponse {

	@JsonProperty("job_id")
	@JsonAlias({"jobId"})
	public Integer getJobId() {
		return this.getInteger("job_id");
	}

	public void setJobId(Integer jobId) {
		this.set("job_id", jobId);
	}

	@JsonProperty("url")
	public String getUrl() {
		return this.getString("url");
	}

	public void setUrl(String url) {
		this.set("url", url);
	}
}