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

/**
 * Response model for the following API calls:
 * <p>
 * POST /api/{version}/messages/{message_type}/language/{lang}/actions/export
 * <br>
 * POST /api/{version}/messages/{message_type}/actions/import
 */
public class BulkTranslationJobResponse extends VaultResponse {

	@JsonProperty("data")
	public Data getData() {
		return (Data) this.get("data");
	}

	public void setData(Data data) {
		this.set("data", data);
	}

	public class Data extends VaultModel {
		@JsonProperty("job_id")
		@JsonAlias({"jobId"})
		public String getJobId() {
			return this.getString("job_id");
		}

		public void setJobId(String jobId) {
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
}