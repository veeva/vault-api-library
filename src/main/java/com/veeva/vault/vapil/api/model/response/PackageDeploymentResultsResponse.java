/*---------------------------------------------------------------------
 *	Copyright (c) 2021 Veeva Systems Inc.  All Rights Reserved.
 *	This code is based on pre-existing content developed and
 *	owned by Veeva Systems Inc. and may only be used in connection
 *	with the deliverable with which it was provided to Customer.
 *---------------------------------------------------------------------
 */
package com.veeva.vault.vapil.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veeva.vault.vapil.api.model.common.PackageCode;
import com.veeva.vault.vapil.api.model.VaultModel;
import com.veeva.vault.vapil.api.model.common.PackageLog;
import com.veeva.vault.vapil.api.model.common.PackageStep;

import java.util.List;

/**
 * Model for the following API calls responses:
 * <p>
 * GET https://login.veevavault.com/auth/discovery
 * <p>
 * Discover the authentication type of a user. With this API,
 * applications can dynamically adjust the the login requirements per user,
 * and support either username/password or OAuth2.0 / OpenID Connect authentication schemes.
 */
public class PackageDeploymentResultsResponse extends VaultResponse {

	@JsonProperty("responseDetails")
	public ResponseDetails getResponseDetails() {
		return (ResponseDetails) this.get("responseDetails");
	}

	public void setResponseDetails(ResponseDetails responseDetails) {
		this.set("responseDetails", responseDetails);
	}

	public static class ResponseDetails extends VaultModel {

		@JsonProperty("total_steps")
		public Integer getTotalSteps() {
			return this.getInteger("total_steps");
		}

		public void setTotalSteps(Integer totalSteps) {
			this.set("total_steps", totalSteps);
		}

		@JsonProperty("deployed")
		public Integer getDeployedComponents() {
			return this.getInteger("deployed_components");
		}

		public void setDeployedComponents(Integer deployedComponents) {
			this.set("deployed_components", deployedComponents);
		}

		@JsonProperty("deployment_log")
		public List<PackageLog> getDeploymentLog() {
			return (List<PackageLog>) this.get("deployment_log");
		}

		public void setDeploymentLog(List<PackageLog> deploymentLog) {
			this.set("deployment_log", deploymentLog);
		}

		@JsonProperty("package_status__v")
		public String getPackageStatus() {
			return this.getString("package_status__v");
		}

		public void setPackageStatus(String packageStatus) {
			this.set("package_status__v", packageStatus);
		}

		@JsonProperty("deployed_with_error")
		public Integer getDeployedWithError() {
			return this.getInteger("deployed_with_error");
		}

		public void setDeployedWithError(Integer deployedWithError) {
			this.set("deployed_with_error", deployedWithError);
		}

		@JsonProperty("skipped")
		public Integer getSkipped() {
			return this.getInteger("skipped");
		}

		public void setSkipped(Integer skipped) {
			this.set("skipped", skipped);
		}

		@JsonProperty("failed")
		public Integer getFailed() {
			return this.getInteger("failed");
		}

		public void setFailed(Integer failed) {
			this.set("failed", failed);
		}

		@JsonProperty("deployed_with_warnings")
		public Integer getDeployedWithWarnings() {
			return this.getInteger("deployed_with_warnings");
		}

		public void setDeployedWithWarnings(Integer deployedWithWarnings) {
			this.set("deployed_with_warnings", deployedWithWarnings);
		}

		@JsonProperty("deployed_with_failures")
		public Integer getDeployedWithFailures() {
			return this.getInteger("deployed_with_failures");
		}

		public void setDeployedWithFailures(Integer deployedWithFailures) {
			this.set("deployed_with_failures", deployedWithFailures);
		}

		@JsonProperty("package_steps")
		public List<PackageStep> getPackageSteps() { return (List<PackageStep>) this.get("package_steps"); }

		public void setPackageSteps(List<PackageStep> packageSteps) { this.set("package_steps", packageSteps); }

		@JsonProperty("package_code")
		public List<PackageCode> getPackageCode() { return (List<PackageCode>) this.get("package_code"); }

		public void setPackageCode(List<PackageCode> packageCode) { this.set("package_code", packageCode); }
	}
}