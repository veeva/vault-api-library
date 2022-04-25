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
import com.veeva.vault.vapil.api.model.common.PackageStep;
import com.veeva.vault.vapil.api.model.response.VaultResponse;

import java.util.List;


/**
 * Response model for the following API calls:
 * <p>
 * POST /api/{version}/services/package/actions/validate
 * <br>
 * POST /api/{version}/services/vobject/vault_package__v/{package_id}/actions/validate
 */
public class ValidatePackageResponse extends VaultResponse {

	@JsonProperty("responseDetails")
	public ResponseDetails getResponseDetails() {
		return (ResponseDetails) this.get("responseDetails");
	}

	public void setResponseDetails(ResponseDetails responseDetails) {
		this.set("responseDetails", responseDetails);
	}

	public static class ResponseDetails extends VaultModel {

		@JsonProperty("author")
		public String getAuthor() {
			return this.getString("author");
		}

		public void setAuthor(String author) {
			this.set("author", author);
		}

		@JsonProperty("end_time")
		public String getEndTime() {
			return this.getString("end_time");
		}

		public void setEndTime(String endTime) {
			this.set("end_time", endTime);
		}

		@JsonProperty("package_error")
		public String getPackageError() {
			return this.getString("package_error");
		}

		public void setPackageError(String packageError) {
			this.set("package_error", packageError);
		}

		@JsonProperty("package_name")
		public String getPackageName() {
			return this.getString("package_name");
		}

		public void setPackageName(String packageName) {
			this.set("package_name", packageName);
		}

		@JsonProperty("package_status")
		public String getPackageStatus() {
			return this.getString("package_status");
		}

		public void setPackageStatus(String packageStatus) {
			this.set("package_status", packageStatus);
		}

		@JsonProperty("package_steps")
		public List<PackageStep> getPackageSteps() {
			return (List<PackageStep>) this.get("package_steps");
		}

		public void setPackageSteps(List<PackageStep> packageSteps) {
			this.set("package_steps", packageSteps);
		}

		@JsonProperty("source_vault")
		public String getSourceVault() {
			return this.getString("source_vault");
		}

		public void setSourceVault(String sourceVault) {
			this.set("source_vault", sourceVault);
		}

		@JsonProperty("start_time")
		public String getStartTime() {
			return this.getString("start_time");
		}

		@JsonProperty("start_time")
		public void setStartTime(String startTime) {
			this.set("start_time", startTime);
		}

		@JsonProperty("summary")
		public String getSummary() {
			return this.getString("summary");
		}

		public void setSummary(String summary) {
			this.set("summary", summary);
		}

		@JsonProperty("total_steps")
		public Integer getTotalSteps() {
			return this.getInteger("total_steps");
		}

		public void setTotalSteps(Integer totalSteps) {
			this.set("total_steps", totalSteps);
		}

	}
}