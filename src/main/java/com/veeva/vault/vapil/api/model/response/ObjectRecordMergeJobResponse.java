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
import com.veeva.vault.vapil.api.model.common.ObjectAttachment;

import java.util.List;

/**
 * Model for the following API calls responses:
 * <p>
 * POST /api/{version}/vobjects/{object_name}/actions/merge
 */
public class ObjectRecordMergeJobResponse extends VaultResponse {

	@JsonProperty("data")
	public Job getData() {
		return (Job) this.get("data");
	}

	public void setData(Job data) {
		this.set("data", data);
	}

	public static class Job extends VaultModel {

		@JsonProperty("job_id")
		public Integer getJobId() {
			return this.getInteger("job_id");
		}

		public void setJobId(Integer jobId) {
			this.set("job_id", jobId);
		}
	}
}