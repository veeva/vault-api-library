/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

/**
 * Model for Retrieve Binder Export Results
 * <br>
 * GET /api/{version}/objects/binders/batch/actions/export/{jobid}/results
 */
public class BinderExportResponse extends VaultResponse {

	@JsonProperty("file")
	public String getFile() {
		return this.getString("file");
	}

	public void setFile(String file) {
		this.set("file", file);
	}

	@JsonProperty("id")
	public Integer getId() {
		return this.getInteger("id");
	}

	public void setId(Integer id) {
		this.set("id", id);
	}

	@JsonProperty("job_id")
	public Integer getJobId() {
		return this.getInteger("job_id");
	}

	public void setJobId(Integer jobId) {
		this.set("job_id", jobId);
	}

	@JsonProperty("major_version_number__v")
	public Integer getMajorVersionNumber() {
		return this.getInteger("major_version_number__v");
	}

	public void setMajorVersionNumber(Integer majorVersionNumber) {
		this.set("major_version_number__v", majorVersionNumber);
	}

	@JsonProperty("minor_version_number__v")
	public Integer getMinorVersionNumber() {
		return this.getInteger("minor_version_number__v");
	}

	public void setMinorVersionNumber(Integer minorVersionNumber) {
		this.set("minor_version_number__v", minorVersionNumber);
	}

	@JsonProperty("user_id__v")
	public Integer getUserId() {
		return this.getInteger("user_id__v");
	}

	public void setUserId(Integer userId) {
		this.set("user_id__v", userId);
	}
}