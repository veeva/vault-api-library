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
 * Response model for the following API calls:
 * <p>
 * POST /api/{version}/services/jobs/actions/cancel
 */
public class JobCancelBulkResponse extends VaultResponse {

	@JsonProperty("data")
	public List<JobCancelResponse> getData() {
		return (List<JobCancelResponse>) this.get("data");
	}

	public void setData(List<JobCancelResponse> data) {
		this.set("data", data);
	}

	public static class JobCancelResponse extends VaultModel {
		@JsonProperty("data")
		public JobCancel getData() {
			return (JobCancel) this.get("data");
		}

		public void setData(JobCancel data) {
			this.set("data", data);
		}

		@JsonProperty("responseStatus")
		public String getResponseStatus() {
			return (String) this.get("responseStatus");
		}

		public void setResponseStatus(String responseStatus) {
			this.set("responseStatus", responseStatus);
		}

		@JsonProperty("errors")
		public Error getErrors() {return (Error) this.get("errors");}

		public void setErrors(Error errors) {this.set("errors", errors);}


		public static class JobCancel extends VaultModel {
			@JsonProperty("id")
			public Integer getId() {
				return this.getInteger("id");
			}

			public void setId(Integer id) {
				this.set("id", id);
			}
		}

		public static class Error extends VaultModel {
			@JsonProperty("type")
			public String getType() {
				return this.getString("type");
			}

			public void setType(String type) {
				this.set("type", type);
			}

			@JsonProperty("message")
			public String getMessage() {
				return this.getString("message");
			}

			public void setMessage(String message) {
				this.set("message", message);
			}
		}
	}
}